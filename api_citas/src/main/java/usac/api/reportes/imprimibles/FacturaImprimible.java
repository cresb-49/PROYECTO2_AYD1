/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes.imprimibles;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import usac.api.models.Factura;
import usac.api.reportes.Reporte;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class FacturaImprimible extends Reporte {

    private Factura factura;

    public byte[] init(Factura factura, String tipoExportacion) throws Exception {
        this.factura = factura;
        //si pasaron las comporbaciones mandamos a traer los parametros
        Map<String, Object> parametrosReporte = this.construirComprobante();
        //mandamos ha abrir el reporte
        return this.exportarReporte("Factura", parametrosReporte,
                tipoExportacion);
    }

    private Map<String, Object> construirComprobante() throws Exception {
        //crear el mapa que contendra los parametros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        String fechaFactura
                = this.manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        this.factura.getCreatedAt().toLocalDate()
                );

        Double subTotal = this.obtenerSubtotal(
                this.factura.getTotal(),
                this.factura.getReserva().getAdelanto());

        parametrosReporte.put("idFactura", "" + this.factura.getId());
        parametrosReporte.put("total", "Q " + this.factura.getTotal());
        parametrosReporte.put("adelanto", "Q " + this.factura.getReserva().getAdelanto());
        parametrosReporte.put("fechaFactura", fechaFactura);
        parametrosReporte.put("subtotal", "Q " + subTotal);
        parametrosReporte.put("datosCliente", this.getDatosCliente());
        parametrosReporte.put("detalle", this.getDetalle());

        return parametrosReporte;
    }

    private Double obtenerSubtotal(Double total, Double adelanto) {
        return total - adelanto;
    }

    private String getDatosCliente() {
        return String.format(""
                + "<b>%s</b><br/>"
                + "<b>CUI: </b>%s<br/>"
                + "<b>NIT: </b>%s",
                this.factura.getNombre(),
                this.factura.getReserva().getReservador().getCui(),
                this.factura.getNit()
        );
    }

    private String getDetalle() {

        String descripcion;
        String nombreServicio;
        String empleadoNombre;

        if (this.factura.getReserva().getReservaCancha() != null) {
            nombreServicio = String.format("Reserva cancha %s",
                    this.factura.getReserva().getReservaCancha().getCancha().getId());
            empleadoNombre = "N/A";
        } else if (this.factura.getReserva().getReservaServicio() != null) {
            nombreServicio = this.factura.getReserva().getReservaServicio().getServicio().getNombre();
            empleadoNombre = this.factura.getReserva().getReservaServicio().getEmpleado().getUsuario().getNombres()
                    + " " + this.factura.getReserva().getReservaServicio().getEmpleado().getUsuario().getApellidos();
        } else {
            nombreServicio = "Error";
            empleadoNombre = "Error";
        }

        descripcion = String.format("Servicio: %s, Lo atendera: %s",
                nombreServicio, empleadoNombre);

        String detalle = String.format(""
                + "<b>Concepto:</b>: %s<br/>"
                + "<b>Reserva Id:</b>: %s<br/>"
                + "<b>Descripcion:</b>: %s<br/>"
                + "<b>Cantidad:</b>: 1<br/>"
                + "<b>Adelanto:</b>: Q %s<br/>"
                + "<b>Precio del servicio:</b>: Q %s<br/>",
                this.factura.getConcepto(),
                this.factura.getId(),
                descripcion,
                this.factura.getReserva().getAdelanto(),
                this.factura.getReserva().getTotalACobrar());
        return detalle;
    }
}
