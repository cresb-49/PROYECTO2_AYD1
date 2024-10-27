/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import usac.api.models.dto.reportes.ClienteFrecuenteDto;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteClientesImprimible;
import usac.api.repositories.FacturaRepository;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteCientesFrecuentes extends Reporte {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private ReporteClientesImprimible reporteClientesImprimible;

    public ReporteClientesFrecuentesDto generarCientesFrecuentes(ReporteRequest reporteRequest) {

        List<ClienteFrecuenteDto> clientesFrecuentes = facturaRepository.
                findClientesFrecuentes(Date.valueOf(reporteRequest.getFecha1()),
                        Date.valueOf(reporteRequest.getFecha2()));

        int numeroVentas = clientesFrecuentes.size();

        Double total = clientesFrecuentes.stream().
                mapToDouble(ClienteFrecuenteDto::getValorTotalCompras).sum();
        //crear el dto del reporte
        return new ReporteClientesFrecuentesDto(clientesFrecuentes,
                numeroVentas,
                total,
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        reporteRequest.getFecha1()),
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        reporteRequest.getFecha2())
        );
    }

    public byte[] exportarClientesFrecuentes(ReporteExportRequest request) throws Exception {
        // Generar el reporte de ventas
        ReporteClientesFrecuentesDto reporte = generarCientesFrecuentes(request);

        // Construir el reporte utilizando JasperReports
        return this.reporteClientesImprimible.init(reporte, request.getTipoExporte());

    }
}
