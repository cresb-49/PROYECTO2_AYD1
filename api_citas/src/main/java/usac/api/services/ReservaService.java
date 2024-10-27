package usac.api.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.Auditor;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.Empleado;
import usac.api.models.Factura;
import usac.api.models.HorarioCancha;
import usac.api.models.HorarioEmpleado;
import usac.api.models.Reserva;
import usac.api.models.ReservaCancha;
import usac.api.models.ReservaServicio;
import usac.api.models.Servicio;
import usac.api.models.Usuario;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.ReservaDTO;
import usac.api.models.request.GetReservacionesRequest;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.reportes.imprimibles.FacturaImprimible;
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
    @Autowired
    @Lazy
    private FacturaService facturaService;
    @Autowired
    private FacturaImprimible facturaImprimible;
    @Autowired
    private ComprobanteReservaImprimible comprobanteReservaImprimible;

    @Autowired
    private ReporteService reporteService;

    /**
     * Realiza la reserva de una cancha.
     *
     * @param reservacionCanchaRequest Objeto que contiene los detalles de la
     *                                 reserva de cancha.
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
        String diaReserva = this.manejadorFechas
                .localDateANombreDia(reservacionCanchaRequest.getFechaReservacion());
        Dia dia = this.diaService.getDiaByNombre(diaReserva);

        // Vamos a obtener el horario de la cancha y verificar que las horas sean de
        // acuerdo al horario
        HorarioCancha obtenerHorarioPorDiaYCancha = this.horarioCanchaService.obtenerHorarioPorDiaYCancha(dia,
                cancha);

        verificarHoras(reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin(),
                obtenerHorarioPorDiaYCancha);

        // Verificar disponibilidad horaria de la cancha
        List<ReservaCancha> reservasSolapadas = reservaCanchaRepository.findReservasSolapadas(
                reservacionCanchaRequest.getFechaReservacion(),
                reservacionCanchaRequest.getCanchaId(),
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        verificarDisponibilidadHoraria(reservasSolapadas,
                "La cancha ya está ocupada en el horario solicitado.");

        // Obtener el usuario reservador desde el JWT
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        // verificar que el reservador no tenga reservas en el mismo horario
        Long reservasDelReservasor = this.reservaRepository.countReservasDelReservadorSolapadas(
                reservador.getId(), reservacionCanchaRequest.getFechaReservacion(),
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        if (reservasDelReservasor > 0) {
            throw new Exception("Ya tienes una reservación en el mismo periodo.");
        }

        // Calcular pagos de acuerdo a las horas de reserva y el precio de la cancha
        Double porcentajeAdelanto = this.negocioService.obtenerNegocio().getPorcentajeAnticipo();
        Long horas = this.manejadorFechas.calcularDiferenciaEnHoras(
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());
        Double precioHoraCancha = cancha.getCostoHora();

        double[] pagos = calcularPagos(porcentajeAdelanto, horas, precioHoraCancha);

        // Crear la entidad de reserva
        Reserva reserva = crearReserva(reservador, reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin(), reservacionCanchaRequest.getFechaReservacion(),
                pagos[0], pagos[1]);

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
     *                                   de servicio.
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

        // Calculamos la hora de fin al hacer la sumatoria de la hora de inicio y el
        // tiempo estimado del servicio
        LocalTime horaFin = this.manejadorFechas.sumarHoras(
                reservacionServicioRequest.getHoraInicio(), servicio.getDuracion());
        reservacionServicioRequest.setHoraFin(horaFin); // seteamos la hora de fin del servicio

        // Obtener el usuario reservador desde el JWT
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        // verificar que el reservador no tenga reservas en el mismo horario
        Long reservasDelReservasor = this.reservaRepository.countReservasDelReservadorSolapadas(
                reservador.getId(), reservacionServicioRequest.getFechaReservacion(),
                reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin());

        if (reservasDelReservasor > 0) {
            throw new Exception("Ya tienes una reservación en el mismo periodo.");
        }

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

        // Verificar que el empleado asignado tenga el rol adecuado para realizar el
        // servicio comparando el ID
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

        // Calcular los montos a pagar
        Double porcentajeAdelanto = this.negocioService.obtenerNegocio().getPorcentajeAnticipo();
        Double precioServicio = servicio.getCosto();
        double[] pagos = calcularPagos(porcentajeAdelanto, 1L, precioServicio);

        // Crear la entidad de reserva
        Reserva reserva = crearReserva(reservador, reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin(),
                reservacionServicioRequest.getFechaReservacion(),
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
     * Cancela una reserva existente y genera una factura por la cancelación.
     *
     * <p>
     * Este método realiza las siguientes operaciones:
     * </p>
     * <ul>
     * <li>Valida el ID de la reserva.</li>
     * <li>Verifica que el usuario que solicita la cancelación sea el mismo que
     * hizo la reserva.</li>
     * <li>Cancela la reserva, guardando la fecha de cancelación.</li>
     * <li>Genera una factura por el monto del adelanto debido a la
     * cancelación.</li>
     * <li>Genera y devuelve un archivo PDF con los detalles de la factura.</li>
     * </ul>
     *
     * @param reservaId El ID de la reserva a cancelar.
     * @return Un objeto {@link ArchivoDTO} que contiene el PDF generado de la
     *         factura de cancelación.
     * @throws Exception Si la reserva no se encuentra, el ID es inválido, o el
     *                   usuario no tiene permiso para cancelar la reserva.
     */
    @Transactional(rollbackOn = Exception.class)
    public ArchivoDTO cancelarReserva(Long reservaId) throws Exception {
        this.validarId(new Auditor(reservaId), "El id de la reserva invalido.");
        // obtenemos la reserva segun su id
        Reserva reserva = this.reservaRepository.findById(reservaId).orElseThrow(
                () -> new Exception("Reserva no encontrada."));
        this.isDesactivated(reserva);
        this.isDeleted(reserva);

        if (reserva.getCanceledAt() != null) {
            throw new Exception("La reservacion ya ha sido cancelada.");
        }
        if (reserva.getRealizada()) {
            throw new Exception("La reservacion ya ha sido realizada.");
        }

        // ahora debemos verificar que el usuario que esta intentando a cancelar sea el
        // mismo que reservo
        Usuario usuario = this.usuarioService.getUsuarioUseJwt();

        if (!Objects.equals(usuario.getId(), reserva.getReservador().getId())) {
            throw new Exception("No puedes cancelar una reservacion que no es tuya.");
        }

        // ahora cancelamos la cita
        reserva.setCanceledAt(LocalDateTime.now());
        // ahora guardamos la edicion de la cita
        Reserva saveReserva = this.reservaRepository.save(reserva);

        // ahora debemos generar la factura por el concepto de "Cancelacion de cita" y
        // por el monto del adelanto
        Factura factura = new Factura(
                usuario.getNombres() + " " + usuario.getApellidos(),
                usuario.getNit(),
                "Cancelación de cita",
                "",
                reserva.getAdelanto());

        Factura facturaSave = this.facturaService.crearFactura(factura,
                saveReserva.getId());// mandar a guardar la factura

        // ahora actualizamos la reservacion para que percista la relacion
        saveReserva.setFactura(facturaSave);
        this.reservaRepository.save(saveReserva);

        // generar el pdf de la factura
        byte[] reporte = this.facturaImprimible.init(factura, "pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FileHttpMetaData.PDF.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder(FileHttpMetaData.PDF.getContentDispoition())
                .filename(FileHttpMetaData.PDF.getFileName())
                .build());

        // retornar el archivo
        return new ArchivoDTO(headers, reporte);
    }

    /**
     * Marca una reserva como realizada y genera una factura por la cita
     * realizada.
     *
     * <p>
     * Este método valida que la reserva no haya sido cancelada o ya realizada
     * previamente. Si la reserva es válida, se marca como realizada y se genera
     * una factura asociada.
     *
     * @param reservaId El ID de la reserva a marcar como realizada.
     * @return Un objeto {@link ArchivoDTO} que contiene el reporte de la
     *         factura en formato PDF.
     * @throws Exception Si la reserva no es encontrada, ya fue cancelada, o ya
     *                   fue realizada.
     */
    @Transactional(rollbackOn = Exception.class)
    public ArchivoDTO realizarLaReserva(Long reservaId) throws Exception {
        this.validarId(new Auditor(reservaId), "El id de la reserva invalido.");
        // obtenemos la reserva segun su id
        Reserva reserva = this.reservaRepository.findById(reservaId).orElseThrow(
                () -> new Exception("Reserva no encontrada."));
        this.isDesactivated(reserva);
        this.isDeleted(reserva);

        if (reserva.getCanceledAt() != null) {
            throw new Exception("La reservacion ya ha sido cancelada.");
        }

        if (reserva.getRealizada()) {
            throw new Exception("La reservacion ya ha sido realizada.");
        }

        // ahora cancelamos la cita
        reserva.setRealizada(true);
        // ahora guardamos la edicion de la cita
        Reserva saveReserva = this.reservaRepository.save(reserva);

        // ahora debemos generar la factura por el concepto de "Cancelacion de cita" y
        // por el monto del adelanto
        Factura factura = new Factura(
                reserva.getReservador().getNombres() + " " + reserva.getReservador().getApellidos(),
                reserva.getReservador().getNit(),
                "Cita realizada",
                "",
                reserva.getTotalACobrar());

        Factura facturaSave = this.facturaService.crearFactura(factura,
                saveReserva.getId());// mandar a guardar la factura

        // ahora actualizamos la reservacion para que percista la relacion
        saveReserva.setFactura(facturaSave);
        this.reservaRepository.save(saveReserva);

        // generar el pdf de la factura
        byte[] reporte = this.facturaImprimible.init(factura, "pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FileHttpMetaData.PDF.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder(FileHttpMetaData.PDF.getContentDispoition())
                .filename(FileHttpMetaData.PDF.getFileName())
                .build());

        // retornar el archivo
        return new ArchivoDTO(headers, reporte);
    }

    /**
     * Obtiene una reserva por su ID.
     *
     * @param reservaId El ID de la reserva.
     * @return La reserva correspondiente al ID.
     * @throws Exception si no se encuentra la reserva.
     */
    public Reserva obtenerReservaPorId(Long reservaId) throws Exception {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(
                () -> new Exception("No se encontró la reserva con el ID proporcionado: " + reservaId));
        return reserva;
    }

    /**
     * Obtiene una reserva por su ID y la convierte a un objeto {@link ReservaDTO}.
     * 
     * @param reservaId
     * @return
     * @throws Exception
     */
    public ReservaDTO obtenerReservaDTO(Long reservaId) throws Exception {
        Reserva reserva = this.obtenerReservaPorId(reservaId);
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(reserva.getId());
        Usuario user = new Usuario();
        user.setId(reserva.getReservador().getId());
        user.setNombres(reserva.getReservador().getNombres());
        user.setApellidos(reserva.getReservador().getApellidos());
        user.setNit(reserva.getReservador().getNit());
        reservaDTO.setReservador(user);
        reservaDTO.setHoraInicio(reserva.getHoraInicio());
        reservaDTO.setHoraFin(reserva.getHoraFin());
        reservaDTO.setFechaReservacion(reserva.getFechaReservacion());
        reservaDTO.setIdFactura(reserva.getFactura() != null ? reserva.getFactura().getId() : null);
        reservaDTO.setRealizada(reserva.getRealizada());
        reservaDTO.setCanceledAt(reserva.getCanceledAt());
        reservaDTO.setAdelanto(reserva.getAdelanto());
        reservaDTO.setTotalACobrar(reserva.getTotalACobrar());
        reservaDTO.setReservaCancha(reserva.getReservaCancha());
        reservaDTO.setReservaServicio(reserva.getReservaServicio());
        return reservaDTO;
    }

    /**
     * Genera un comprobante de reserva en formato PDF basado en el ID de una
     * reserva específica.
     * <p>
     * Este método busca la reserva correspondiente al ID proporcionado. Si se
     * encuentra, se genera un comprobante de reserva en formato PDF utilizando
     * la clase {@code comprobanteReservaImprimible}. Luego, se configura el
     * objeto {@link HttpHeaders} con los metadatos adecuados para la respuesta
     * HTTP, incluyendo el tipo de contenido (PDF).
     * </p>
     *
     * @param reservaId El ID de la reserva para la cual se debe generar el
     *                  comprobante.
     * @return {@link ArchivoDTO} que contiene los bytes del comprobante PDF y
     *         los headers HTTP correspondientes.
     * @throws Exception Si no se encuentra la reserva con el ID proporcionado o
     *                   si ocurre algún error durante la generación del
     *                   comprobante.
     */
    public ArchivoDTO obtenerComprobanteReservaPorId(Long reservaId) throws Exception {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow(
                () -> new Exception("No se encontró la reserva con el ID proporcionado: " + reservaId));
        byte[] reporte = comprobanteReservaImprimible.init(reserva, "pdf");
        HttpHeaders headers = reporteService.setHeaders(FileHttpMetaData.PDF);
        return new ArchivoDTO(headers, reporte);
    }

    /**
     * Crea una entidad Reserva con los datos proporcionados.
     */
    private Reserva crearReserva(Usuario reservador, LocalTime horaInicio, LocalTime horaFin,
            LocalDate fechaReservacion, Double adelanto, Double totalACobrar) {
        return new Reserva(
                reservador,
                horaInicio,
                horaFin,
                fechaReservacion,
                null, // Factura opcional
                false,
                adelanto,
                totalACobrar);
    }

    /**
     * Genera un reporte en formato PDF para una reserva y prepara los headers
     * para la respuesta HTTP.
     */
    private ArchivoDTO generarReporteReserva(Reserva reserva) throws Exception {
        byte[] reporte = this.reservaImprimible.init(reserva, "pdf");
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
        return new double[] { adelanto, totalAPagar };
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

        // Filtrar empleados que trabajen el día solicitado y estén disponibles en el
        // horario
        Empleado empleadoAsignado = empleadosDisponibles.stream()
                .filter(empleado -> {
                    // Verificar si el empleado trabaja ese día
                    HorarioEmpleado horarioEmpleado = this.empleadoService
                            .obtenerHorarioDiaEmpleado(dia, empleado);
                    if (horarioEmpleado != null) {
                        try {
                            // Verificar que el horario solicitado esté dentro de los
                            // horarios del empleado
                            this.verificarHoras(reservacionServicioRequest.getHoraInicio(),
                                    reservacionServicioRequest.getHoraFin(),
                                    horarioEmpleado);
                            this.verificarDisponibilidadEmpleado(empleado,
                                    reservacionServicioRequest);
                            return true;
                        } catch (Exception e) {
                            // Si no está disponible en el horario, pasar al siguiente
                            // empleado
                            return false;
                        }
                    }
                    return false;
                })
                .min(Comparator.comparing(empleado -> getCantidadReservasEmpleado(empleado,
                        reservacionServicioRequest)))
                .orElseThrow(() -> new Exception(
                        "No hay empleados disponibles para este servicio en el día y horario solicitado."));

        return empleadoAsignado;
    }

    /**
     * Retorna la cantidad de reservas que tiene un empleado en la fecha
     * solicitada.
     */
    private Long getCantidadReservasEmpleado(Empleado empleado,
            ReservacionServicioRequest reservacionServicioRequest) {
        return reservaServicioRepository.countReservasByEmpleadoAndFecha(
                empleado.getId(),
                reservacionServicioRequest.getFechaReservacion());
    }

    /**
     * Verifica si el empleado tiene disponibilidad en el horario solicitado.
     */
    private void verificarDisponibilidadEmpleado(Empleado empleado,
            ReservacionServicioRequest reservacionServicioRequest) throws Exception {
        List<ReservaServicio> reservas = reservaServicioRepository.findReservasSolapadasEmpleado(
                reservacionServicioRequest.getFechaReservacion(),
                empleado.getId(),
                reservacionServicioRequest.getHoraInicio(),
                reservacionServicioRequest.getHoraFin());
        verificarDisponibilidadHoraria(reservas, "El empleado ya tiene una reserva en el horario solicitado.");
    }

    /**
     * Verifica que la hora de inicio sea menor a la hora de fin.
     */
    private void verificarHoras(LocalTime horaInicio, LocalTime horaFin, HorarioCancha horarioCancha)
            throws Exception {
        if (!this.manejadorFechas.esPrimeraHoraAntes(horaInicio, horaFin)) {
            throw new Exception("La hora de inicio no puede ser mayor a la hora de fin.");
        }
        if (!this.manejadorFechas.isHorarioEnLimites(horarioCancha.getApertura(), horarioCancha.getCierre(),
                horaInicio, horaFin)) {
            throw new Exception(String.format(
                    "La reserva no puede realizarse fuera de los horarios permitidos. El horario de la cancha es de %s a %s.",
                    horarioCancha.getApertura().toString(), horarioCancha.getCierre().toString()));
        }
    }

    /**
     * Verifica que la hora de inicio sea menor a la hora de fin para un
     * empleado.
     */
    private void verificarHoras(LocalTime horaInicio, LocalTime horaFin, HorarioEmpleado horarioEmpleado)
            throws Exception {

        if (!this.manejadorFechas.esPrimeraHoraAntes(horaInicio, horaFin)) {
            throw new Exception("La hora de inicio no puede ser mayor a la hora de fin.");
        }

        if (!this.manejadorFechas.isHorarioEnLimites(horarioEmpleado.getEntrada(),
                horarioEmpleado.getSalida(), horaInicio, horaFin)) {

            throw new Exception(String.format(
                    "La reserva no puede realizarse fuera de los horarios del empleado. El horario del empleado es de %s a %s.",
                    horarioEmpleado.getEntrada().toString(),
                    horarioEmpleado.getSalida().toString()));
        }
    }

    /**
     * Obtiene una lista de reservaciones que pertenecen al mes y año
     * especificados.
     *
     * @param request Objeto que contiene el mes y el año para filtrar las
     *                reservaciones.
     * @return Lista de reservaciones correspondientes al mes y año
     *         proporcionados.
     * @throws Exception Si el modelo del request es inválido o ocurre un error
     *                   en la búsqueda.
     */
    public List<Reserva> getReservasDelMes(GetReservacionesRequest request) throws Exception {
        this.validarModelo(request);
        Usuario usuario = usuarioService.getUsuarioUseJwt();

        // Verificar el rol del usuario y aplicar la consulta correspondiente
        if (usuario.getRoles().stream().anyMatch(rol -> rol.getRol().getNombre().equals("CLIENTE"))) {

            // Si es cliente, buscar reservas donde él sea el reservador en ese mes y año
            return reservaRepository.findReservasByReservadorAndMesAndAnio(usuario.getId(),
                    request.getMes(),
                    request.getAnio());

        } else if (usuario.getRoles().stream().anyMatch(rol -> rol.getRol().getNombre().equals("ADMIN"))) {

            // Si es administrador, buscar todas las reservas del mes
            return reservaRepository.findReservasByMesAndAnio(request.getMes(), request.getAnio());

        } else if (usuario.getRoles().stream().anyMatch(rol -> rol.getRol().getNombre().equals("EMPLEADO"))) {

            // Si es empleado, buscar reservas donde él esté asignado para atender
            return reservaRepository.findReservasByEmpleadoAndMesAndAnio(usuario.getId(),
                    request.getMes(),
                    request.getAnio());

        } else {
            throw new Exception("El usuario no tiene permisos para ver las reservas.");
        }
    }

    /**
     * Obtiene una lista de reservaciones que pertenecen al mes y año
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public List<ReservaDTO> getReservasDelMesResponse(GetReservacionesRequest request) throws Exception {
        List<Reserva> reservas = this.getReservasDelMes(request);
        List<ReservaDTO> reservasDTO = new java.util.ArrayList<>();
        for (Reserva reserva : reservas) {
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setId(reserva.getId());
            Usuario user = new Usuario();
            user.setId(reserva.getReservador().getId());
            user.setNombres(reserva.getReservador().getNombres());
            user.setApellidos(reserva.getReservador().getApellidos());
            user.setNit(reserva.getReservador().getNit());
            reservaDTO.setReservador(user);
            reservaDTO.setHoraInicio(reserva.getHoraInicio());
            reservaDTO.setHoraFin(reserva.getHoraFin());
            reservaDTO.setFechaReservacion(reserva.getFechaReservacion());
            reservaDTO.setIdFactura(reserva.getFactura() != null ? reserva.getFactura().getId() : null);
            reservaDTO.setRealizada(reserva.getRealizada());
            reservaDTO.setCanceledAt(reserva.getCanceledAt());
            reservaDTO.setAdelanto(reserva.getAdelanto());
            reservaDTO.setTotalACobrar(reserva.getTotalACobrar());
            reservaDTO.setReservaCancha(reserva.getReservaCancha());
            reservaDTO.setReservaServicio(reserva.getReservaServicio());
            reservasDTO.add(reservaDTO);
        }
        return reservasDTO;
    }

}
