/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes.imprimibles;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import usac.api.models.Reserva;
import usac.api.reportes.Reporte;

/**
 *
 * @author luid
 */
@Component
public class ComprobanteReservaImprimible extends Reporte {

    private Reserva reserva;

    public byte[] init(Reserva reserva) throws Exception {
        this.reserva = reserva;
        //si pasaron las comporbaciones mandamos a traer los parametros
        Map<String, Object> parametrosReporte = this.construirComprobante();
        //mandamos ha abrir el reporte
        return this.exportarReporte("ConstanciaReserva", parametrosReporte,
                "pdf");
    }

    private Map<String, Object> construirComprobante() throws Exception {
        //crear el mapa que contendra los parametros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        String nombreServicio;
        String empleadoNombre;
        String estadoCita;
        String fechaYHora = String.format("%s, de %s a %s",
                this.manejadorTiempo.parsearFechaYHoraAFormatoRegional(this.reserva.getFechaReservacion()),
                this.reserva.getHoraInicio(),
                this.reserva.getHoraFin()
        );

        if (this.reserva.getReservaCancha() != null) {
            nombreServicio = String.format("Reserva cancha %s", this.reserva.getReservaCancha().getCancha().getId());
            empleadoNombre = "N/A";
        } else if (this.reserva.getReservaServicio() != null) {
            nombreServicio = this.reserva.getReservaServicio().getServicio().getNombre();
            empleadoNombre = this.reserva.getReservaServicio().getEmpleado().getUsuario().getNombres();
        } else {
            nombreServicio = "Error";
            empleadoNombre = "Error";
        }

        if (this.reserva.getRealizada()) {
            estadoCita = "Realizada";
        } else if (this.reserva.getCanceledAt() != null) {
            estadoCita = "Cancelada";
        } else {
            estadoCita = "Pendiente";
        }

        parametrosReporte.put("nombre", this.reserva.getReservador().getNombres());
        parametrosReporte.put("apellidos", this.reserva.getReservador().getApellidos());
        parametrosReporte.put("adelanto", "Q " + this.reserva.getAdelanto());
        parametrosReporte.put("idReserva", this.reserva.getId());
        parametrosReporte.put("nombreServicio", nombreServicio);
        parametrosReporte.put("empleadoNombre", empleadoNombre);
        parametrosReporte.put("estadoCita", estadoCita);
        parametrosReporte.put("fechaHora", fechaYHora);

        return parametrosReporte;
    }

}
