/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes.imprimibles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.stereotype.Component;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.reportes.Reporte;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteServiciosImprimible extends Reporte {

    private ReporteServiciosDTO reporteServiciosDTO;

     public byte[] init(ReporteServiciosDTO reporteServiciosDTO, String tipoExportacion) throws Exception {
        this.reporteServiciosDTO = reporteServiciosDTO;

        // Obtener los parámetros necesarios para el reporte
        Map<String, Object> parametrosReporte = this.construirReporteServicios();

        // Exportar el reporte con los parámetros obtenidos
        return this.exportarReporte("ReporteServicios", parametrosReporte, tipoExportacion);
    }

    private Map<String, Object> construirReporteServicios() throws Exception {
        // Crear el mapa que contendrá los parámetros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        // Convertir la lista de facturas a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource tablaServicios = new JRBeanArrayDataSource(
                reporteServiciosDTO.getServicios().toArray());

        JRBeanArrayDataSource tablaCanchas = new JRBeanArrayDataSource(
                reporteServiciosDTO.getCanchas().toArray());

        parametrosReporte.put("servicios", tablaServicios);
        parametrosReporte.put("canchas", tablaCanchas);
        parametrosReporte.put("noReservaciones",
                reporteServiciosDTO.getNumeroReservaciones());
        parametrosReporte.put("fecha1", reporteServiciosDTO.getFecha1());
        parametrosReporte.put("fecha2", reporteServiciosDTO.getFecha2());
        parametrosReporte.put("fecha",
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(LocalDate.now()));

        return parametrosReporte;
    }

}
