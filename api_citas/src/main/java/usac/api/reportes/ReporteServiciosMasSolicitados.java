/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ServicioMasDemandadoDto;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteServiciosImprimible;
import usac.api.repositories.ReservaRepository;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteServiciosMasSolicitados extends Reporte {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReporteServiciosImprimible reporteServiciosImprimible;

    public ReporteServiciosDTO generarServiciosFrecuentes(ReporteRequest reporteRequest) {

        //obtenemos los servicios mas solicitados
        List<ServicioMasDemandadoDto> servicios = reservaRepository.
                findServiciosMasDemandados(
                        Date.valueOf(reporteRequest.getFecha1()),
                        Date.valueOf(reporteRequest.getFecha2()));
        //traer las canchas
        List<ServicioMasDemandadoDto> canchas = reservaRepository.
                findCanchasMasDemandadas(
                        Date.valueOf(reporteRequest.getFecha1()),
                        Date.valueOf(reporteRequest.getFecha2()));

        //ahora debemos construir el total de las reservaciones hechas en ese lapso de tiempo
        int totalReservaciones = servicios.size() + canchas.size();

        //retornarmos el dto del reporte
        return new ReporteServiciosDTO(servicios, canchas,
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(reporteRequest.getFecha1()),
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(reporteRequest.getFecha2()),
                totalReservaciones);
    }

    public byte[] exportarServiciosFrecuentes(ReporteExportRequest request) throws Exception {
        // Generar el reporte de ventas
        ReporteServiciosDTO reporte = generarServiciosFrecuentes(request);
        // Construir el reporte utilizando JasperReports
        return this.reporteServiciosImprimible.init(reporte, request.getTipoExporte());

    }
}
