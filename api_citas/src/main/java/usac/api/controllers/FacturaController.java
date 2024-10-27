/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.Factura;
import usac.api.models.dto.ArchivoDTO;
import usac.api.services.FacturaService;
import usac.api.services.permisos.ValidadorPermiso;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 *
 * @author Luis Monterroso
 */
@RestController
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ValidadorPermiso validadorPermiso;

    @Operation(summary = "Obtiene el comprobante de una factura por su ID",
            description = "Recupera el archivo del comprobante de una factura específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArchivoDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/cliente/facturaPorId/{facturaId}")
    public ResponseEntity<?> comprobanteReservaPorId(
            @Parameter(description = "id de la factura a buscar.", required = true)
            @PathVariable Long facturaId) {
        try {
            // Invoca el método para obtener las reservas
            ArchivoDTO data = facturaService.obtenerFacturaPorIdCliente(facturaId);
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

    @Operation(summary = "Obtiene todas las facturas del cliente", description = "Recupera una lista de todas las facturas del cliente autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de facturas del cliente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Factura.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/cliente/facturasCliente")
    public ResponseEntity<?> comprobanteReservaPorId() {
        try {
            // Invoca el método para obtener las reservas
            List<Factura> data = facturaService.getFacturasCliente();
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data,
                    null, null)
                    .sendResponse();
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

    @GetMapping("/private/restricted/facturas")
    public ResponseEntity<?> getFacturas() {
        try {
            validadorPermiso.verificarPermiso();
            // Invoca el método para obtener las reservas
            List<Factura> data = facturaService.getFacturasAdmin();
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data,
                    null, null)
                    .sendResponse();
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

    @GetMapping("/private/restricted/facturaPorId/{facturaId}")
    public ResponseEntity<?> getFacturaPorIdAdmin(
            @Parameter(description = "id de la factura a buscar.", required = true)
            @PathVariable Long facturaId) {
        try {
            validadorPermiso.verificarPermiso();
            // Invoca el método para obtener las reservas
            ArchivoDTO data = facturaService.obtenerFacturaPorIdAdmin(facturaId);
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }
}
