/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.time.LocalTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.models.Reserva;
import usac.api.models.ReservaCancha;
import usac.api.models.Usuario;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.repositories.ReservaCanchaRepository;
import usac.api.repositories.ReservaRepository;
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
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioService usuarioService;

    @Transactional(rollbackOn = Exception.class)
    public ArchivoDTO reservaCancha(ReservacionCanchaRequest reservacionCanchaRequest) throws Exception {
        this.validarModelo(reservacionCanchaRequest);

        Cancha cancha = this.canchaService.getCanchaById(reservacionCanchaRequest.getCanchaId());

        // Obtenemos el día de la reserva
        String diaReserva = this.manejadorFechas.localDateANombreDia(reservacionCanchaRequest.getFechaReservacion());

        // Buscamos el día según el nombre
        Dia dia = this.diaService.getDiaByNombre(diaReserva);

        // Traemos el horario de la cancha en el día de la reserva
        HorarioCancha obtenerHorarioPorDiaYCancha = this.horarioCanchaService.obtenerHorarioPorDiaYCancha(dia, cancha);

        if (!this.manejadorFechas.esPrimeraHoraAntes(
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin())) {
            throw new Exception(
                    "La hora de inicio no puede ser mayor a la hora de fin.");
        }

        // Verificamos si las horas de la reserva están dentro del horario de apertura y cierre de la cancha
        if (!this.manejadorFechas.isHorarioEnLimites(obtenerHorarioPorDiaYCancha.getApertura(),
                obtenerHorarioPorDiaYCancha.getCierre(), reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin())) {
            throw new Exception(String.format("La reserva no puede realizarse fuera de los horarios permitidos. "
                    + "El horario de la cancha es de %s a %s.",
                    obtenerHorarioPorDiaYCancha.getApertura().toString(),
                    obtenerHorarioPorDiaYCancha.getCierre().toString()));
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

        // Obtener el usuario a partir del JWT
        Usuario reservador = this.usuarioService.getUsuarioUseJwt();

        // Calcular adelanto (ejemplo: 10%)
        Double porcentajeAdelanto = 10.00;

        // Calcular horas totales de la reservación
        Long horas = this.manejadorFechas.calcularDiferenciaEnHoras(
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin());

        // Traer el precio por hora de la cancha y calcular el total a pagar
        Double precioHoraCancha = cancha.getCostoHora();
        Double totalAPagar = horas * precioHoraCancha;
        Double adelanto = totalAPagar * (porcentajeAdelanto / 100);

        // Crear la entidad `Reserva`
        Reserva reserva = new Reserva(
                reservador,
                reservacionCanchaRequest.getHoraInicio(),
                reservacionCanchaRequest.getHoraFin(),
                reservacionCanchaRequest.getFechaReservacion(),
                null, // Aquí puedes asignar una factura si la tienes
                false,
                adelanto,
                totalAPagar
        );

        // Guardar primero la entidad `Reserva`
        Reserva saveReserva = reservaRepository.save(reserva);

        // Crear la entidad `ReservaCancha`
        ReservaCancha reservaCancha = new ReservaCancha(saveReserva, cancha);

        // Establecer la relación bidireccional
        saveReserva.setReservaCancha(reservaCancha);

        // Guardar ambas entidades
        reservaCanchaRepository.save(reservaCancha);
        reservaRepository.save(saveReserva); // Guardar de nuevo para actualizar la relación

        // Generar la constancia a partir de la reserva generada
        byte[] reporte = this.reservaImprimible.init(saveReserva);

        // Preparar los headers para la respuesta HTTP
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.parseMediaType(FileHttpMetaData.PDF.getContentType())); // Ajustar el valor de Content-Type
        headers.setContentDisposition(ContentDisposition.builder(FileHttpMetaData.PDF.getContentDispoition()) // Corregir el contentDisposition
                .filename(FileHttpMetaData.PDF.getFileName())
                .build());
        // Retornar el archivo generado
        return new ArchivoDTO(
                headers,
                reporte
        );
    }

}
