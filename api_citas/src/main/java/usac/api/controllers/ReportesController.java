package usac.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.services.ReporteService;
import usac.api.services.permisos.ValidadorPermiso;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 * Controlador para la gestión de reportes.
 * <p>
 * Este controlador contiene los endpoints que permiten generar y exportar
 * reportes en varios formatos. Se accede a estos reportes a través de
 * peticiones protegidas mediante autenticación y permisos.
 * </p>
 *
 * Endpoints principales:
 * <ul>
 * <li>{@link #exportarReporte(ReporteExportRequest)}: Exporta un reporte en
 * varios formatos (PDF, Excel, Word, Imagen).</li>
 * <li>{@link #reporteVentas(ReporteRequest)}: Genera un reporte de ventas
 * basado en un rango de fechas.</li>
 * </ul>
 *
 * @see ReporteService
 * @see ArchivoDTO
 * @see ReporteExportRequest
 * @see ReporteRequest
 * @see ReporteVentasDTO
 *
 * @author Luis Monterroso
 */
@RestController
@RequestMapping("/api/reporte")
public class ReportesController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ValidadorPermiso validadorPermiso;

    /**
     * Exportar un reporte en varios formatos (PDF, Excel, Word, Imagen).
     * <p>
     * Este endpoint recibe una solicitud con los detalles del tipo de report e
     * y formato, y genera el archivo correspondiente.
     * </p>
     *
     * @param request El cuerpo de la solicitud que contiene los datos para la
     * exportación.
     * @return El archivo exportado en el formato solicitado.
     */
    @Operation(summary = "Exportar Reporte",
            description = "Permite exportar un reporte en formato PDF, Excel, Word o Imagen.",
            security = {
                @SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Reporte exportado correctamente",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ArchivoDTO.class))}),
        @ApiResponse(responseCode = "500",
                description = "Error interno del servidor",
                content = @Content)
    })
    @PostMapping("/private/restricted/exportarReporte")
    public ResponseEntity<?> exportarReporte(
            @RequestBody ReporteExportRequest request
    ) {
        try {
            validadorPermiso.verificarPermiso();
            ArchivoDTO data = reporteService.exportarReporte(request);
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /**
     * Generar un reporte de ventas con los datos solicitados.
     * <p>
     * Este endpoint genera un reporte de ventas basado en un rango de fechas
     * proporcionado en el cuerpo de la solicitud.
     * </p>
     *
     * @param request El cuerpo de la solicitud que contiene las fechas de
     * inicio y fin para el reporte de ventas.
     * @return El DTO con los datos del reporte de ventas.
     */
    @Operation(summary = "Generar Reporte de Ventas",
            description = "Genera un reporte de ventas basado en las fechas proporcionadas.",
            security = {
                @SecurityRequirement(name = "bearerAuth")})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Reporte de ventas generado correctamente",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReporteVentasDTO.class))}),
        @ApiResponse(responseCode = "500",
                description = "Error interno del servidor",
                content = @Content)
    })
    @PostMapping("/private/restricted/reporteVentas")
    public ResponseEntity<?> reporteVentas(
        @RequestBody ReporteRequest request
    ) {
        try {
            validadorPermiso.verificarPermiso();
            ReporteVentasDTO data = reporteService.getReporteVentas(request);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
