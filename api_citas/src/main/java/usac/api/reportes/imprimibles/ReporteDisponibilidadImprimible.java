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
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.reportes.Reporte;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteDisponibilidadImprimible extends Reporte {

    private ReporteDisponibilidadDTO disponibilidad;

    public byte[] init(ReporteDisponibilidadDTO disponibilidad, String tipoExportacion) throws Exception {
        this.disponibilidad = disponibilidad;

        // Obtener los parámetros necesarios para el reporte
        Map<String, Object> parametrosReporte = this.construirReporteDisponibilidad();

        // Exportar el reporte con los parámetros obtenidos
        return this.exportarReporte("ReporteDisp", parametrosReporte, tipoExportacion);
    }

    private Map<String, Object> construirReporteDisponibilidad() throws Exception {

        // Crear el mapa que contendrá los parámetros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        // Convertir la lista de facturas a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource tablaEmpleados = new JRBeanArrayDataSource(
                disponibilidad.getDisponibilidadEmpleados().toArray());

        JRBeanArrayDataSource tablaCanchas = new JRBeanArrayDataSource(
                disponibilidad.getDisponibilidadCanchas().toArray());

        parametrosReporte.put("tablaEmpleados", tablaEmpleados);
        parametrosReporte.put("tablaCanchas", tablaCanchas);
        parametrosReporte.put("noReservaciones",
                disponibilidad.getDisponibilidadCanchas().size()
                + disponibilidad.getDisponibilidadEmpleados().size());
        parametrosReporte.put("fecha1", disponibilidad.getFecha1());
        parametrosReporte.put("fecha2", disponibilidad.getFecha2());
        parametrosReporte.put("fecha",
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        LocalDate.now()));

        return parametrosReporte;
    }
}
