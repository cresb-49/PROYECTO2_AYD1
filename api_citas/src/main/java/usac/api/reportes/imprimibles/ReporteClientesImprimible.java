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
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.reportes.Reporte;

/**
 * Clase que gestiona la generación y exportación del reporte de clientes
 * frecuentes. Este reporte incluye la lista de los clientes más frecuentes
 * junto con el resumen de ventas y fechas. El reporte se genera utilizando
 * JasperReports y puede exportarse a varios formatos como PDF, Excel, Word o
 * imagen.
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteClientesImprimible extends Reporte {

    /**
     * DTO que contiene la información de los clientes frecuentes a ser
     * utilizada en el reporte.
     */
    private ReporteClientesFrecuentesDto clientes;

    /**
     * Inicializa el proceso de generación del reporte de clientes frecuentes,
     * compilando el reporte Jasper con los datos proporcionados y exportándolo
     * en el formato solicitado.
     *
     * @param clientes DTO que contiene la lista de clientes frecuentes y el
     * resumen de ventas.
     * @param tipoExportacion Formato en el que se desea exportar el reporte
     * (PDF, Excel, etc.).
     * @return Un arreglo de bytes que contiene el reporte exportado en el
     * formato solicitado.
     * @throws Exception Si ocurre algún error durante la generación o
     * exportación del reporte.
     */
    public byte[] init(ReporteClientesFrecuentesDto clientes, String tipoExportacion) throws Exception {
        this.clientes = clientes;
        // Si pasaron las comprobaciones, se construyen los parámetros del reporte
        Map<String, Object> parametrosReporte = this.construirClientesFrecuentes();
        // Se abre y exporta el reporte
        return this.exportarReporte("ReporteClientes", parametrosReporte, tipoExportacion);
    }

    /**
     * Construye los parámetros necesarios para el reporte de clientes
     * frecuentes. Los parámetros incluyen los datos de los clientes, las fechas
     * del reporte y el resumen total de ventas.
     *
     * @return Un mapa que contiene los parámetros a ser utilizados por
     * JasperReports.
     * @throws Exception Si ocurre algún error al construir los parámetros.
     */
    private Map<String, Object> construirClientesFrecuentes() throws Exception {
        // Crear el mapa de parámetros para el reporte
        Map<String, Object> parametrosReporte = new HashMap<>();

        // Convertir la lista de clientes a un JRBeanArrayDataSource para JasperReports
        JRBeanArrayDataSource datosCliente = new JRBeanArrayDataSource(clientes.getClienteFrecuentes().toArray());

        // Agregar parámetros clave al mapa
        parametrosReporte.put("datosCliente", datosCliente);
        parametrosReporte.put("total", "Q " + clientes.getTotalVentas());
        parametrosReporte.put("noVentas", "" + clientes.getNumeroVentas());
        parametrosReporte.put("fecha1", clientes.getFecha1());
        parametrosReporte.put("fecha2", clientes.getFecha2());

        // Agregar la fecha actual en formato regional
        parametrosReporte.put("fecha", manejadorTiempo.parsearFechaYHoraAFormatoRegional(LocalDate.now()));

        return parametrosReporte;
    }

}
