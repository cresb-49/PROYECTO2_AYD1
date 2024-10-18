/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes.imprimibles;

import java.util.HashMap;
import java.util.Map;
import usac.api.models.Reserva;
import usac.api.reportes.Reporte;

/**
 *
 * @author luid
 */
public class ComprobanteReservaImprimible extends Reporte {

    private Reserva reserva;

    public byte[] init(Reserva reserva) throws Exception {
        this.reserva = reserva;
        //si pasaron las comporbaciones mandamos a traer los parametros
        Map<String, Object> parametrosReporte = this.construirComprobante();
        //mandamos ha abrir el reporte
        return this.exportarReporte("Comprobante", parametrosReporte,
                "pdf");
    }

    private Map<String, Object> construirComprobante() throws Exception {
        //crear el mapa que contendra los parametros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();
        
        return parametrosReporte;
    }

}
