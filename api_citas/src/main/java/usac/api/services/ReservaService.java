/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.models.Reserva;
import usac.api.models.ReservaCancha;
import usac.api.models.Usuario;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.repositories.ReservaCanchaRepository;
import usac.api.tools.ManejadorTiempo;

/**
 *
 * @author luid
 */
@org.springframework.stereotype.Service
public class ReservaService extends Service {

    @Autowired
    private ComprobanteReservaImprimible reservaImprimible;
    @Autowired
    private ReservaCanchaRepository reservaCanchaRepository;
    @Autowired
    private ManejadorTiempo manejadorFechas;
    @Autowired
    private HorarioCanchaService horarioCanchaService;
    @Autowired
    private DiaService diaService;
    @Autowired
    private CanchaService canchaService;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional(rollbackOn = Exception.class)
    public byte[] reservaCancha(ReservacionCanchaRequest reservacionCanchaRequest) throws Exception {
        this.validarModelo(reservacionCanchaRequest);
        Cancha cancha = this.canchaService.getCanchaById(reservacionCanchaRequest.getCanchaId());
        //obtenemos el dia de la reserva
        String diaReserva = this.manejadorFechas.localDateANombreDia(reservacionCanchaRequest.getFechaReservacion());
        //mandamos a buscar el dia segun el nombre que nos arroje la funcion
        Dia dia = this.diaService.getDiaByNombre(diaReserva);
        //mandamos a traer el horario de la cancha en el dia de la reserva
        HorarioCancha obtenerHorarioPorDiaYCancha = this.horarioCanchaService.obtenerHorarioPorDiaYCancha(dia, cancha);
        // Verificamos si las horas de la reserva están dentro de los horarios de apertura y cierre de la cancha
        if (!this.manejadorFechas.isHorarioEnLimites(obtenerHorarioPorDiaYCancha.getApertura(),
                obtenerHorarioPorDiaYCancha.getCierre(), reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin())) {

            // Formateamos un mensaje de excepción con los horarios de apertura y cierre
            String mensajeError = String.format("La reserva no puede realizarse fuera de los horarios permitidos. "
                    + "El horario de la cancha es de %s a %s.",
                    obtenerHorarioPorDiaYCancha.getApertura().toString(),
                    obtenerHorarioPorDiaYCancha.getCierre().toString());

            throw new Exception(mensajeError);
        }

        // Verificar si la cancha está ocupada en el horario solicitado
        List<ReservaCancha> reservasSolapadas = reservaCanchaRepository.findReservasSolapadas(
                reservacionCanchaRequest.getFechaReservacion(),
                reservacionCanchaRequest.getCanchaId(),
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        if (!reservasSolapadas.isEmpty()) {
            throw new Exception("La cancha ya está ocupada en el horario solicitado.");
        }
        //obtener el usuario por medio del jwt
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        //mandamos a traer el porcentaje de adelanto de la aplicacion
        Double porcentajeAdelanto = 10.00;

        //obtenemos las horas tottales de la reservacion
        Long horas = this.manejadorFechas.calcularDiferenciaEnHoras(
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        //traer el precio por hora de la cancha 
        Double precioHoraCancha = cancha.getCostoHora();

        //cacular el recio total y sacar el adelanto
        Double totalAPagar = horas * precioHoraCancha;
        Double adelanto = totalAPagar * (porcentajeAdelanto / 100);

        //si esta disponible entonces hacemos la reservacion creando la constancia de reserva, y cobramos el porcentaje de anticipo
        Reserva reserva = new Reserva(
                reservador,
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin(),
                reservacionCanchaRequest.getFechaReservacion(),
                null,
                false,
                adelanto,
                totalAPagar
        );

        // Guardar la reserva de la cancha
        ReservaCancha reservaCancha = new ReservaCancha(reserva, cancha);
        reservaCanchaRepository.save(reservaCancha);
        //ahora debemos generar la constacia a partir de la reserva generada
        return this.reservaImprimible.init(reserva);
    }
}
