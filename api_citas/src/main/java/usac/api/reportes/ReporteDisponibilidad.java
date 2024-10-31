/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes;

import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.reportes.DisponiblilidadRecursoDTO;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteDisponibilidadImprimible;
import usac.api.repositories.ReservaRepository;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteDisponibilidad extends Reporte {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ReporteDisponibilidadImprimible reporteDisponibilidadImprimible;

    public ReporteDisponibilidadDTO generarReporteDisponiblilidad(ReporteRequest reporteRequest) {

        List<DisponiblilidadRecursoDTO> empleados
                = reservaRepository.findReporteDisponibilidadReservasServicio(
                        reporteRequest.getFecha1(),
                        reporteRequest.getFecha2());

        List<DisponiblilidadRecursoDTO> canchas
                = reservaRepository.findReporteDisponibilidadReservasCancha(
                        reporteRequest.getFecha1(),
                        reporteRequest.getFecha2());

        for (DisponiblilidadRecursoDTO item : empleados) {
            item.setFechaReservacionStr(
                    manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                            item.getFechaReservacion()
                    ));
        }

        for (DisponiblilidadRecursoDTO item : canchas) {
            item.setFechaReservacionStr(
                    manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                            item.getFechaReservacion()
                    ));
        }

        return new ReporteDisponibilidadDTO(empleados,
                canchas,
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        reporteRequest.getFecha1()),
                manejadorTiempo.parsearFechaYHoraAFormatoRegional(
                        reporteRequest.getFecha2()));
    }

    public byte[] exportarReporteDisponiblilidad(ReporteExportRequest request) throws Exception {
        // Generar el reporte de ventas
        ReporteDisponibilidadDTO reporte = generarReporteDisponiblilidad(request);
        // Construir el reporte utilizando JasperReports
        return reporteDisponibilidadImprimible.init(reporte, request.getTipoExporte());
    }

}
