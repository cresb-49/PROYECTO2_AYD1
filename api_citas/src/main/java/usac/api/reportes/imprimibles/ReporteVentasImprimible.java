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
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.reportes.Reporte;

/**
 * Clase que genera la exportación del reporte de ventas utilizando
 * JasperReports. Se encarga de preparar los datos y parámetros necesarios para
 * generar el informe y exportarlo a formatos como PDF o Excel.
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteVentasImprimible extends Reporte {

    private ReporteVentasDTO ventasDTO;

    /**
     * Inicializa la generación del reporte de ventas. Toma los datos del DTO y
     * el tipo de exportación (PDF, Excel, etc.) para crear el informe.
     *
     * @param ventasDTO DTO que contiene los datos del reporte de ventas.
     * @param tipoExportacion Tipo de exportación deseada (por ejemplo, PDF o
     * Excel).
     * @return Un arreglo de bytes que representa el reporte exportado en el
     * formato seleccionado.
     * @throws Exception Si ocurre un error durante la creación o exportación
     * del reporte.
     */
    public byte[] init(ReporteVentasDTO ventasDTO, String tipoExportacion) throws Exception {
        this.ventasDTO = ventasDTO;

        // Obtener los parámetros necesarios para el reporte
        Map<String, Object> parametrosReporte = this.construirReporteVentas();

        // Exportar el reporte con los parámetros obtenidos
        return this.exportarReporte("ReporteVentasPY2", parametrosReporte, tipoExportacion);
    }

    /**
     * Construye el mapa de parámetros necesarios para generar el reporte de
     * ventas. Este método convierte los datos del DTO en parámetros
     * comprensibles por JasperReports, incluyendo la lista de facturas y
     * totales.
     *
     * @return Un mapa con los parámetros requeridos para generar el reporte de
     * ventas.
     * @throws Exception Si ocurre un error al crear los parámetros.
     */
    private Map<String, Object> construirReporteVentas() throws Exception {
        // Crear el mapa que contendrá los parámetros del reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        // Convertir la lista de facturas a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource tablaVentas = new JRBeanArrayDataSource(ventasDTO.getFacturas().toArray());

        // Agregar parámetros al mapa
        parametrosReporte.put("tablaVentas", tablaVentas);
        parametrosReporte.put("total", "Q " + ventasDTO.getTotal());
        parametrosReporte.put("noVentas", "" + ventasDTO.getNoVentas());
        parametrosReporte.put("adelanto", "Q " + ventasDTO.getTotalAdelantos());
        parametrosReporte.put("subtotal", "Q " + ventasDTO.getTotalNoAdelanto());
        parametrosReporte.put("fecha1", ventasDTO.getFecha1());
        parametrosReporte.put("fecha2", ventasDTO.getFecha2());
        parametrosReporte.put("fecha",
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(LocalDate.now()));

        return parametrosReporte;
    }
}
