/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.reportes.DisponiblilidadRecursoDTO;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.ReporteCientesFrecuentes;
import usac.api.reportes.ReporteDisponibilidad;
import usac.api.reportes.ReporteServiciosMasSolicitados;
import usac.api.reportes.ReporteVentas;

/**
 * Servicio para la gestión de la exportación y generación de reportes.
 *
 * Esta clase proporciona métodos para exportar diferentes tipos de reportes
 * (como ventas) en varios formatos, incluyendo Excel, Word, imágenes y PDF.
 *
 * También gestiona la configuración de metadatos HTTP (headers) según el tipo
 * de archivo exportado.
 *
 * Métodos principales: - exportarReporte: Genera y exporta el reporte en el
 * formato solicitado. - getReporteVentas: Genera el reporte de ventas para el
 * lapso de tiempo especificado.
 *
 * El servicio utiliza los reportes disponibles, como ReporteVentas, para
 * obtener los datos y luego prepara el archivo exportable con los headers
 * correctos según el tipo de exportación.
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class ReporteService extends Service {

    @Autowired
    private ReporteVentas reporteVentas;
    @Autowired
    private ReporteCientesFrecuentes reporteCientesFrecuentes;
    @Autowired
    private ReporteServiciosMasSolicitados reporteServiciosMasSolicitados;
    @Autowired
    private ReporteDisponibilidad reporteDisponibilidad;

    /**
     * Exporta un reporte basado en los parámetros recibidos en el request.
     *
     * Según el tipo de reporte y el formato de exportación (PDF, Excel, Word,
     * Imagen), el servicio genera el archivo y lo devuelve en un objeto
     * ArchivoDTO con los headers HTTP configurados correctamente.
     *
     * @param req Objeto que contiene la información necesaria para generar el
     * reporte, como el tipo de reporte y el formato de exportación.
     * @return ArchivoDTO que contiene los bytes del archivo exportado y los
     * headers HTTP correspondientes.
     * @throws Exception Si ocurre algún error durante la generación o
     * exportación del reporte.
     */
    public ArchivoDTO exportarReporte(ReporteExportRequest req) throws Exception {
        validarModelo(req);
        byte[] reporteBytes;
        HttpHeaders headers;

        // Generar el reporte según el tipo solicitado
        switch (req.getTipoReporte()) {
            case "reporteVentas" -> {
                reporteBytes = reporteVentas.exportarReporteVentas(req);
            }
            case "reporteClientes" -> {
                reporteBytes = reporteCientesFrecuentes.exportarClientesFrecuentes(req);
            }
            case "reporteServicios" -> {
                reporteBytes = reporteServiciosMasSolicitados.exportarServiciosFrecuentes(req);
            }

            case "reporteDisponibilidad" -> {
                reporteBytes = reporteDisponibilidad.exportarReporteDisponiblilidad(req);
            }
            default -> {
                throw new Exception("Tipo de reporte no válido.");
            }
        }

        // Configurar los headers según el tipo de exportación solicitado
        switch (req.getTipoExporte()) {
            case "excel" -> {
                headers = setHeaders(FileHttpMetaData.EXCEL);
            }
            case "word" -> {
                headers = setHeaders(FileHttpMetaData.WORD);
            }
            case "img" -> {
                headers = setHeaders(FileHttpMetaData.IMG);
            }
            default -> {
                headers = setHeaders(FileHttpMetaData.PDF);
            }
        }

        return new ArchivoDTO(headers, reporteBytes);
    }

    /**
     * Genera el reporte de ventas basado en los parámetros de tiempo
     * proporcionados en el request.
     *
     * @param request Objeto que contiene las fechas de inicio y fin para el
     * reporte.
     * @return ReporteVentasDTO que contiene el resumen de ventas, incluyendo
     * total de ventas, número de ventas, adelantos y otros datos relevantes.
     */
    public ReporteVentasDTO getReporteVentas(ReporteRequest request) throws Exception {
        validarModelo(request);
        return reporteVentas.generarReporteVentas(request);
    }

    public ReporteClientesFrecuentesDto getReporteClientesFrecuentes(ReporteRequest request) throws Exception {
        validarModelo(request);
        return reporteCientesFrecuentes.generarCientesFrecuentes(request);
    }

    public ReporteServiciosDTO getReporteServicios(ReporteRequest request) throws Exception {
        validarModelo(request);
        return reporteServiciosMasSolicitados.generarServiciosFrecuentes(request);
    }

    public ReporteDisponibilidadDTO getReporteDisponibilidad(ReporteRequest request) throws Exception {
        validarModelo(request);
        return reporteDisponibilidad.generarReporteDisponiblilidad(request);
    }

    /**
     * Configura los headers HTTP necesarios para la respuesta de un archivo,
     * basándose en el tipo de archivo que se va a exportar (PDF, Excel, Word,
     * Imagen).
     *
     * @param meta Metadatos que contienen la información para configurar los
     * headers, como el tipo de contenido y el nombre del archivo.
     * @return HttpHeaders configurados con el tipo de contenido y las
     * disposiciones adecuadas.
     */
    public HttpHeaders setHeaders(FileHttpMetaData meta) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(meta.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder(meta.getContentDispoition())
                .filename(meta.getFileName())
                .build());

        return headers;
    }
}
