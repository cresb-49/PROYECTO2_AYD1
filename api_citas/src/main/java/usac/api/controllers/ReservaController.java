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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.dto.ArchivoDTO;
import usac.api.services.ReservaService;
import usac.api.services.permisos.ValidadorPermiso;

/**
 *
 * @author Luis Monterroso
 */
@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ValidadorPermiso validadorPermiso;

    /**
     * Cancela una reserva de un cliente y devuelve un archivo PDF con la
     * factura por la cancelación.
     *
     * <p>
     * Este método maneja la solicitud PATCH para cancelar una reserva realizada
     * por un cliente. Si la reserva es cancelada exitosamente, se genera y
     * devuelve un PDF con la factura de cancelación.</p>
     *
     * @param idReserva El ID de la reserva que se desea cancelar.
     * @return Un archivo PDF con la factura si la cancelación es exitosa, o un
     * mensaje de error en caso contrario.
     */
    @Operation(summary = "Cancelar una reserva",
            description = "Este endpoint permite a un cliente cancelar una reserva existente. "
            + "Genera y devuelve un archivo PDF con la factura por el monto del adelanto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente, devuelve un archivo PDF con la factura",
                content = @Content(mediaType = "application/pdf")),
        @ApiResponse(responseCode = "400", description = "Error al cancelar la reserva, devuelve un mensaje de error",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/cliente/cancelarReserva/{idReserva}")
    public ResponseEntity<?> reservarServicio(@PathVariable Long idReserva) {
        try {
            // Invoca el método de S
            ArchivoDTO data = reservaService.cancelarReserva(idReserva);
            // Retorna el archivo PDF en la respuesta
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            ex.printStackTrace();
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return ResponseEntity.badRequest().body(ex.getMessage());

        }
    }

    /**
     * Marca una reserva como realizada y genera una factura por la cita.
     *
     * @param reservaId El ID de la reserva que se desea marcar como realizada.
     * @return Un archivo PDF con la factura si la operación es exitosa, o un
     * mensaje de error en caso contrario.
     */
    @Operation(summary = "Marcar reserva como realizada",
            description = "Este endpoint permite marcar una reserva como realizada y genera una factura asociada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva marcada como realizada exitosamente, devuelve un archivo PDF con la factura"),
        @ApiResponse(responseCode = "400", description = "Error al marcar la reserva como realizada, devuelve un mensaje de error")
    })
    @PatchMapping("/private/restricted/realizarReserva/{reservaId}")
    public ResponseEntity<?> realizarReserva(
            @Parameter(description = "ID de la reserva que se desea marcar como realizada", required = true)
            @PathVariable Long reservaId) {
        try {
            this.validadorPermiso.verificarPermiso();
            // Invoca el método para realizar la reserva
            ArchivoDTO archivoDTO = reservaService.realizarLaReserva(reservaId);

            // Retorna el archivo PDF en la respuesta
            return ResponseEntity.ok()
                    .headers(archivoDTO.getHeaders())
                    .body(archivoDTO.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}