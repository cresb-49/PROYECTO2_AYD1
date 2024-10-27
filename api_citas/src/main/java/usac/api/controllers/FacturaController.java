/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.Factura;
import usac.api.models.dto.ArchivoDTO;
import usac.api.services.FacturaService;
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

    @GetMapping("/cliente/facturaPorId/{facturaId}")
    public ResponseEntity<?> comprobanteReservaPorId(
            @Parameter(description = "id de la factura a buscar.", required = true)
            @PathVariable Long facturaId) {
        try {
            // Invoca el método para obtener las reservas
            ArchivoDTO data = facturaService.obtenerFacturaPorId(facturaId);
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

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
}
