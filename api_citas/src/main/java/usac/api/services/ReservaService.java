package usac.api.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.Empleado;
import usac.api.models.HorarioCancha;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Reserva;
import usac.api.models.ReservaCancha;
import usac.api.models.ReservaServicio;
import usac.api.models.Servicio;
import usac.api.models.Usuario;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.repositories.ReservaCanchaRepository;
import usac.api.repositories.ReservaRepository;
import usac.api.repositories.ReservaServicioRepository;
import usac.api.tools.ManejadorTiempo;

/**
 * Servicio para gestionar las operaciones de reserva de canchas y servicios.
 * Esta clase implementa la lógica para crear, verificar y gestionar reservas,
 * tanto para canchas como para servicios.
 */
@org.springframework.stereotype.Service
public class ReservaService extends Service {

    @Autowired
    private ComprobanteReservaImprimible reservaImprimible;
    @Autowired
    private ReservaCanchaRepository reservaCanchaRepository;
    @Autowired
    private ReservaServicioRepository reservaServicioRepository;
    @Autowired
    private ManejadorTiempo manejadorFechas;
    @Autowired
    private HorarioCanchaService horarioCanchaService;
    @Autowired
    private DiaService diaService;
    @Autowired
    private CanchaService canchaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NegocioService negocioService;
    @Autowired
    private ServicioService servicioService;
    @Autowired
    private EmpleadoService empleadoService;

    /**
     * Realiza la reserva de una cancha.
     *
     * @param reservacionCanchaRequest Objeto que contiene los detalles de la
     * reserva de cancha.
     * @return ArchivoDTO que contiene el reporte de la reserva en formato PDF.
     * @throws Exception si ocurre algún error durante el proceso de reserva.
     */
    @Transactional(rollbackOn = Exception.class)
    public ArchivoDTO reservaCancha(ReservacionCanchaRequest reservacionCanchaRequest) throws Exception {
        // Validar el modelo recibido
        this.validarModelo(reservacionCanchaRequest);

        if (reservacionCanchaRequest.getHoraFin() == null) {
            throw new Exception("La hora de fin de la reservacion no puede estar vacia.");
        }

        // Obtener la cancha a partir de su ID
        Cancha cancha = this.canchaService.getCanchaById(reservacionCanchaRequest.getCanchaId());

        // Obtener el día de la reserva y el horario de la cancha
        String diaReserva = this.manejadorFechas.localDateANombreDia(reservacionCanchaRequest.getFechaReservacion());
        Dia dia = this.diaService.getDiaByNombre(diaReserva);

        // Vamos a obtener el horario de la cancha y verificar que las horas sean de acuerdo al horario
        HorarioCancha obtenerHorarioPorDiaYCancha = this.horarioCanchaService.obtenerHorarioPorDiaYCancha(dia, cancha);

        verificarHoras(reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin(),
                obtenerHorarioPorDiaYCancha);

        // Verificar disponibilidad horaria de la cancha
        List<ReservaCancha> reservasSolapadas = reservaCanchaRepository.findReservasSolapadas(
                reservacionCanchaRequest.getFechaReservacion(),
                reservacionCanchaRequest.getCanchaId(),
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        verificarDisponibilidadHoraria(reservasSolapadas, "La cancha ya está ocupada en el horario solicitado.");

        // Obtener el usuario reservador desde el JWT
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        // Calcular pagos de acuerdo a las horas de reserva y el precio de la cancha
        Double porcentajeAdelanto = this.negocioService.obtenerNegocio().getPorcentajeAnticipo();
        Long horas = this.manejadorFechas.calcularDiferenciaEnHoras(
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());
        Double precioHoraCancha = cancha.getCostoHora();

        double[] pagos = calcularPagos(porcentajeAdelanto, horas, precioHoraCancha);

        // Crear la entidad de reserva
        Reserva reserva = crearReserva(reservador, reservacionCanchaRequest.getHoraInicio(), reservacionCanchaRequest.getHoraFin(), reservacionCanchaRequest.getFechaReservacion(), pagos[0], pagos[1]);

        // Guardar la reserva y su relación con la cancha
        Reserva saveReserva = reservaRepository.save(reserva);
        ReservaCancha reservaCancha = new ReservaCancha(saveReserva, cancha);
        saveReserva.setReservaCancha(reservaCancha);
        reservaCanchaRepository.save(reservaCancha);
        reservaRepository.save(saveReserva);

        return generarReporteReserva(saveReserva);
    }

    /**
     * Realiza la reserva de un servicio.
     *
     * @param reservacionServicioRequest Objeto con los detalles de la reserva
     * de servicio.
     * @return ArchivoDTO que contiene el reporte de la reserva en formato PDF.
     * @throws Exception si ocurre algún error durante el proceso de reserva.
     */
    @Transactional(rollbackOn = Exception.class)
    public ArchivoDTO reservaServicio(ReservacionServicioRequest reservacionServicioRequest) throws Exception {
        // Validar el modelo recibido
        this.validarModelo(reservacionServicioRequest);

        // Determinar si la asignación de empleados es aleatoria o manual
        boolean asignacionAleatoria = !this.negocioService.obtenerNegocio().isAsignacionManual();
        Servicio servicio = this.servicioService.getServicioById(
                reservacionServicioRequest.getServicioId());

        // Obtener el servicio y su límite de empleados paralelos
        Integer maxEmpleadosParalelos = servicio.getEmpleadosParalelos();

        // Calculamos la hora de fin al hacer la sumatoria de la hora de inicio y el tiempo estimado del servicio
        LocalTime horaFin = this.manejadorFechas.sumarHoras(
                reservacionServicioRequest.getHoraInicio(), servicio.getDuracion());
        reservacionServicioRequest.setHoraFin(horaFin); // seteamos la hora de fin del servicio

        // Verificar cuántas reservas activas hay para este servicio
        Integer reservasActivas = reservaServicioRepository.countReservasActivasPorServicio(
                reservacionServicioRequest.getServicioId(),
                reservacionServicioRequest.getFechaReservacion(),
                reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin());

        // Verificar si aún hay disponibilidad de empleados
        if (reservasActivas >= maxEmpleadosParalelos) {
            throw new Exception("No hay disponibilidad de empleados para "
                    + "este servicio en el horario seleccionado.");
        }

        // Asignar un empleado de manera aleatoria o manual
        Empleado empleadoAsignado;
        if (asignacionAleatoria) {
            empleadoAsignado = this.asignarEmpleadoAleatorio(servicio, reservacionServicioRequest);
        } else {
            empleadoAsignado = this.empleadoService.getEmpleadoById(
                    reservacionServicioRequest.getEmpleadoId());
        }

        // Verificar que el empleado asignado tenga el rol adecuado para realizar el servicio comparando el ID
        List<Empleado> empleadosDisponibles = this.empleadoService.getEmpleadosPorServicio(
                servicio);

        boolean empleadoAutorizado = empleadosDisponibles.stream()
                .anyMatch(empleado -> empleado.getId().equals(empleadoAsignado.getId()));

        if (!empleadoAutorizado) {
            throw new Exception("El empleado seleccionado no está autorizado "
                    + "para realizar este servicio.");
        }

        // Obtener el día de la semana de la reservación (e.g., Lunes, Martes)
        String diaReserva = this.manejadorFechas.localDateANombreDia(
                reservacionServicioRequest.getFechaReservacion());

        // Buscar el día en la base de datos por su nombre
        Dia dia = this.diaService.getDiaByNombre(diaReserva);

        // Obtener el horario del empleado para el día solicitado
        HorarioEmpleado horarioEmpleado = this.empleadoService.obtenerHorarioDiaEmpleado(dia,
                empleadoAsignado);

        // Verificar que el horario solicitado esté dentro de los horarios del empleado
        this.verificarHoras(reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin(), horarioEmpleado);

        // Verificar la disponibilidad del empleado en el horario solicitado
        verificarDisponibilidadEmpleado(empleadoAsignado, reservacionServicioRequest);

        // Obtener el usuario reservador desde el JWT
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        // Calcular los montos a pagar
        Double porcentajeAdelanto = this.negocioService.obtenerNegocio().getPorcentajeAnticipo();
        Double precioServicio = servicio.getCosto();
        double[] pagos = calcularPagos(porcentajeAdelanto, 1L, precioServicio);

        // Crear la entidad de reserva
        Reserva reserva = crearReserva(reservador, reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin(), reservacionServicioRequest.getFechaReservacion(),
                pagos[0], pagos[1]);

        // Guardar la reserva y su relación con el servicio
        Reserva saveReserva = reservaRepository.save(reserva);
        ReservaServicio reservaServicio = new ReservaServicio(saveReserva, empleadoAsignado, servicio);
        saveReserva.setReservaServicio(reservaServicio);
        reservaServicioRepository.save(reservaServicio);
        reservaRepository.save(saveReserva);

        return generarReporteReserva(saveReserva);
    }

    /**
     * Crea una entidad Reserva con los datos proporcionados.
     */
    private Reserva crearReserva(Usuario reservador, LocalTime horaInicio, LocalTime horaFin, LocalDate fechaReservacion, Double adelanto, Double totalACobrar) {
        return new Reserva(
                reservador,
                horaInicio,
                horaFin,
                fechaReservacion,
                null, // Factura opcional
                false,
                adelanto,
                totalACobrar
        );
    }

    /**
     * Genera un reporte en formato PDF para una reserva y prepara los headers
     * para la respuesta HTTP.
     */
    private ArchivoDTO generarReporteReserva(Reserva reserva) throws Exception {
        byte[] reporte = this.reservaImprimible.init(reserva);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FileHttpMetaData.PDF.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder(FileHttpMetaData.PDF.getContentDispoition())
                .filename(FileHttpMetaData.PDF.getFileName())
                .build());
        return new ArchivoDTO(headers, reporte);
    }

    /**
     * Verifica si el horario solicitado está disponible para un recurso
     * (empleado o cancha).
     */
    private void verificarDisponibilidadHoraria(List<?> reservasSolapadas, String mensajeError) throws Exception {
        if (!reservasSolapadas.isEmpty()) {
            throw new Exception(mensajeError);
        }
    }

    /**
     * Calcula el adelanto y el total a pagar basado en las horas de reserva y
     * el porcentaje de anticipo.
     */
    private double[] calcularPagos(Double porcentajeAdelanto, Long horas, Double precioHora) {
        Double totalAPagar = horas * precioHora;
        Double adelanto = totalAPagar * (porcentajeAdelanto / 100);
        return new double[]{adelanto, totalAPagar};
    }

    /**
     * Asigna un empleado de manera aleatoria balanceando la carga.
     */
    private Empleado asignarEmpleadoAleatorio(Servicio servicio,
            ReservacionServicioRequest reservacionServicioRequest) throws Exception {

        // Obtener la lista de empleados disponibles para el servicio
        List<Empleado> empleadosDisponibles = this.empleadoService.getEmpleadosPorServicio(
                servicio);

        // Obtener el día de la semana de la reservación (e.g., Lunes, Martes)
        String diaReserva = this.manejadorFechas.localDateANombreDia(
                reservacionServicioRequest.getFechaReservacion());

        // Buscar el día en la base de datos por su nombre
        Dia dia = this.diaService.getDiaByNombre(diaReserva);

        // Filtrar empleados que trabajen el día solicitado y estén disponibles en el horario
        Empleado empleadoAsignado = empleadosDisponibles.stream()
                .filter(empleado -> {
                    // Verificar si el empleado trabaja ese día
                    HorarioEmpleado horarioEmpleado = this.empleadoService.
                            obtenerHorarioDiaEmpleado(dia, empleado);
                    if (horarioEmpleado != null) {
                        try {
                            // Verificar que el horario solicitado esté dentro de los horarios del empleado
                            this.verificarHoras(reservacionServicioRequest.getHoraInicio(),
                                    reservacionServicioRequest.getHoraFin(), horarioEmpleado);
                            this.verificarDisponibilidadEmpleado(empleado, reservacionServicioRequest);
                            return true;
                        } catch (Exception e) {
                            // Si no está disponible en el horario, pasar al siguiente empleado
                            return false;
                        }
                    }
                    return false;
                })
                .min(Comparator.comparing(empleado -> getCantidadReservasEmpleado(empleado, reservacionServicioRequest)))
                .orElseThrow(() -> new Exception("No hay empleados disponibles para este servicio en el día y horario solicitado."));

        return empleadoAsignado;
    }

    /**
     * Retorna la cantidad de reservas que tiene un empleado en la fecha
     * solicitada.
     */
    private Long getCantidadReservasEmpleado(Empleado empleado, ReservacionServicioRequest reservacionServicioRequest) {
        return reservaServicioRepository.countReservasByEmpleadoAndFecha(
                empleado.getId(),
                reservacionServicioRequest.getFechaReservacion()
        );
    }

    /**
     * Verifica si el empleado tiene disponibilidad en el horario solicitado.
     */
    private void verificarDisponibilidadEmpleado(Empleado empleado, ReservacionServicioRequest reservacionServicioRequest) throws Exception {
        List<ReservaServicio> reservas = reservaServicioRepository.findReservasSolapadasEmpleado(
                reservacionServicioRequest.getFechaReservacion(),
                empleado.getId(),
                reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin()
        );
        verificarDisponibilidadHoraria(reservas, "El empleado ya tiene una reserva en el horario solicitado.");
    }

    /**
     * Verifica que la hora de inicio sea menor a la hora de fin.
     */
    private void verificarHoras(LocalTime horaInicio, LocalTime horaFin, HorarioCancha horarioCancha) throws Exception {
        if (!this.manejadorFechas.esPrimeraHoraAntes(horaInicio, horaFin)) {
            throw new Exception("La hora de inicio no puede ser mayor a la hora de fin.");
        }
        if (!this.manejadorFechas.isHorarioEnLimites(horarioCancha.getApertura(), horarioCancha.getCierre(), horaInicio, horaFin)) {
            throw new Exception(String.format("La reserva no puede realizarse fuera de los horarios permitidos. El horario de la cancha es de %s a %s.",
                    horarioCancha.getApertura().toString(), horarioCancha.getCierre().toString()));
        }
    }

    /**
     * Verifica que la hora de inicio sea menor a la hora de fin para un
     * empleado.
     */
    private void verificarHoras(LocalTime horaInicio, LocalTime horaFin, HorarioEmpleado horarioEmpleado) throws Exception {

        if (!this.manejadorFechas.esPrimeraHoraAntes(horaInicio, horaFin)) {
            throw new Exception("La hora de inicio no puede ser mayor a la hora de fin.");
        }

        if (!this.manejadorFechas.isHorarioEnLimites(horarioEmpleado.getEntrada(),
                horarioEmpleado.getSalida(), horaInicio, horaFin)) {

            throw new Exception(String.format("La reserva no puede realizarse fuera de los horarios del empleado. El horario del empleado es de %s a %s.",
                    horarioEmpleado.getEntrada().toString(), horarioEmpleado.getSalida().toString()));
        }
    }
}
