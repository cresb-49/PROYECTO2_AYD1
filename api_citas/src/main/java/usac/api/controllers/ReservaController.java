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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.Reserva;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.ReservaDTO;
import usac.api.models.request.GetReservacionesRequest;
import usac.api.services.ReservaService;
import usac.api.services.permisos.ValidadorPermiso;
import usac.api.tools.transformers.ApiBaseTransformer;

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
     * devuelve un PDF con la factura de cancelación.
     * </p>
     *
     * @param idReserva El ID de la reserva que se desea cancelar.
     * @return Un archivo PDF con la factura si la cancelación es exitosa, o un
     *         mensaje de error en caso contrario.
     */
    @Operation(summary = "Cancelar una reserva", description = "Este endpoint permite a un cliente cancelar una reserva existente. "
            + "Genera y devuelve un archivo PDF con la factura por el monto del adelanto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente, devuelve un archivo PDF con la factura", content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "400", description = "Error al cancelar la reserva, devuelve un mensaje de error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
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
     *         mensaje de error en caso contrario.
     */
    @Operation(summary = "Marcar reserva como realizada", description = "Este endpoint permite marcar una reserva como realizada y genera una factura asociada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva marcada como realizada exitosamente, devuelve un archivo PDF con la factura"),
            @ApiResponse(responseCode = "400", description = "Error al marcar la reserva como realizada, devuelve un mensaje de error")
    })
    @PatchMapping("/private/restricted/realizarReserva/{reservaId}")
    public ResponseEntity<?> realizarReserva(
            @Parameter(description = "ID de la reserva que se desea marcar como realizada", required = true) @PathVariable Long reservaId) {
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

    /**
     * Endpoint para obtener las citas correspondientes a un mes y año
     * específicos.
     *
     * @param anio Año en el que se desean obtener las citas.
     * @param mes  Mes en el que se desean obtener las citas.
     * @return ResponseEntity con la lista de citas correspondientes al mes y
     *         año proporcionados, o un mensaje de error en caso de fallar.
     */
    @Operation(description = "Obtiene las citas del mes y año especificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas obtenidas exitosamente", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Reserva.class)) }),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @GetMapping("/protected/getCitasDelMes/{anio}/{mes}")
    public ResponseEntity<?> getCitasDelMes(
            @Parameter(description = "Número de año al que pertenece el mes", required = true) @PathVariable Integer anio,
            @Parameter(description = "Mes donde se quieren ver las citas", required = true) @PathVariable Integer mes) {
        try {
            GetReservacionesRequest req = new GetReservacionesRequest(mes, anio);
            // Invoca el método para obtener las reservas
            // List<Reserva> data = reservaService.getReservasDelMes(req);
            List<ReservaDTO> data = reservaService.getReservasDelMesResponse(req);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

    /**
     * Endpoint protegido para obtener un comprobante de reserva en formato PDF
     * basado en el ID proporcionado.
     * <p>
     * Este método expone una API GET que toma el ID de una reserva como
     * parámetro y devuelve un comprobante de la reserva en formato PDF. Utiliza
     * el servicio de reserva para buscar la reserva y generar el comprobante.
     * En caso de éxito, retorna el archivo PDF junto con los headers
     * configurados. Si ocurre algún error, devuelve una respuesta con un
     * mensaje de error adecuado.
     * </p>
     *
     * @param id El ID de la reserva que se debe buscar.
     * @return {@link ResponseEntity} con el archivo PDF generado en el cuerpo y
     *         los headers configurados para su descarga. En caso de error, retorna
     *         una
     *         respuesta con el estado {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     */
    @Operation(summary = "Obtener comprobante de reserva por ID", description = "Este endpoint protegido genera un comprobante de una"
            + " reserva en formato PDF basándose en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante de reserva generado con éxito.", content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "500", description = "Error al generar el comprobante de reserva.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/cliente/comprobanteReservaPorId/{id}")
    public ResponseEntity<?> comprobanteReservaPorId(
            @Parameter(description = "id de la reserva a buscar.", required = true) @PathVariable Long id) {
        try {
            // Invoca el método para obtener las reservas
            ArchivoDTO data = reservaService.obtenerComprobanteReservaPorId(id);
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, ex.getMessage())
                    .sendResponse();
        }
    }

    @Operation(summary = "Obtener comprobante de reserva por ID", description = "Este endpoint protegido genera un comprobante de una"
            + " reserva en formato PDF basándose en el ID proporcionado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comprobante de reserva generado con éxito.", content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "500", description = "Error al generar el comprobante de reserva.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/private/restricted/comprobanteReservaPorId/{id}")
    public ResponseEntity<?> comprobanteReservaPorIdAdmin(
            @Parameter(description = "id de la reserva a buscar.", required = true) @PathVariable Long id) {
        try {
            validadorPermiso.verificarPermiso();
            // Invoca el método para obtener las reservas
            ArchivoDTO data = reservaService.obtenerComprobanteReservaPorId(id);
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
