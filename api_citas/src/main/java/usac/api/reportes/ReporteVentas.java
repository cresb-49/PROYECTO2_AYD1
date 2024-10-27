/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import usac.api.models.Factura;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteVentasImprimible;
import usac.api.repositories.FacturaRepository;

/**
 * Clase que genera el reporte de ventas en base a las facturas dentro de un
 * rango de fechas.
 *
 * Esta clase también permite exportar el reporte generado a diferentes formatos
 * como PDF o Excel, utilizando la funcionalidad de JasperReports.
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteVentas extends Reporte {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ReporteVentasImprimible reporteVentasImprimible;

    /**
     * Genera el reporte de ventas dentro del rango de fechas especificado en el
     * {@link ReporteRequest}.
     *
     * @param reporteRequest Objeto que contiene las fechas de inicio y fin para
     * filtrar las ventas.
     * @return {@link ReporteVentasDTO} que contiene los datos resumidos del
     * reporte de ventas, como total de ventas, total de adelantos, total sin
     * adelantos y la lista de facturas.
     */
    public ReporteVentasDTO generarReporteVentas(ReporteRequest reporteRequest) {
        LocalDate fecha1 = reporteRequest.getFecha1();
        LocalDate fecha2 = reporteRequest.getFecha2();

        // Obtener las facturas en el rango de fechas
        List<Factura> facturas = facturaRepository.findAllByCreatedAtDateBetween(fecha1, fecha2);

        // Calcular el total de ventas, total de adelantos y otros valores
        double total = facturas.stream().mapToDouble(Factura::getTotal).sum();
        int noVentas = facturas.size();

        // Calcular los adelantos realizados
        double totalAdelantos = facturas.stream()
                .filter(f -> f.getReserva().getAdelanto() != null)
                .mapToDouble(f -> f.getReserva().getAdelanto())
                .sum();

        // Calcular el total sin adelantos
        double totalNoAdelanto = total - totalAdelantos;

        // Convertir las fechas a un formato regional
        String fecha1Str = manejadorTiempo.parsearFechaYHoraAFormatoRegional(fecha1);
        String fecha2Str = manejadorTiempo.parsearFechaYHoraAFormatoRegional(fecha2);

        // Crear y retornar el DTO del reporte
        return new ReporteVentasDTO(total, noVentas, totalAdelantos, totalNoAdelanto, facturas,
                fecha1Str, fecha2Str);
    }

    /**
     * Exporta el reporte de ventas generado a un formato especificado, como PDF
     * o Excel.
     *
     * @param request Objeto {@link ReporteExportRequest} que contiene la
     * información del reporte y el tipo de exportación deseado.
     * @return Un arreglo de bytes que representa el reporte exportado en el
     * formato seleccionado.
     * @throws Exception Si ocurre un error durante la generación o exportación
     * del reporte.
     */
    public byte[] exportarReporteVentas(ReporteExportRequest request) throws Exception {
        // Generar el reporte de ventas
        ReporteVentasDTO reporte = this.generarReporteVentas(request);

        // Construir el reporte utilizando JasperReports
        return this.reporteVentasImprimible.init(reporte, request.getTipoExporte());
    }

}
