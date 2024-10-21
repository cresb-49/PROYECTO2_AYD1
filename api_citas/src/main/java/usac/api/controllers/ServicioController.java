package usac.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usac.api.models.Empleado;
import usac.api.models.Servicio;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.services.ReservaService;
import usac.api.services.ServicioService;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 * Controlador para gestionar servicios relacionados con las reservas de
 * servicios y canchas.
 */
@RestController
@RequestMapping("/api/servicio")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;
    @Autowired
    private ReservaService reservaService;

    @GetMapping("/public/servicios")
    public ResponseEntity<?> getAllServicios() {
        try {
            Object data = servicioService.getServicios();
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("/public/servicio/{id}")
    public ResponseEntity<?> getServicioById(@PathVariable Long id) {
        try {
            System.out.println("id: " + id);
            Object data = servicioService.getServicioById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @GetMapping("/public/servicios/nombre/{nombre}")
    public ResponseEntity<?> getServiciosLikeNombre(@PathVariable String nombre) {
        try {
            Object data = servicioService.getServiciosLikeNombre(nombre);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @DeleteMapping("/private/servicio/{id}")
    public ResponseEntity<?> deleteServicio(@PathVariable Long id) {
        try {
            servicioService.eliminarServicio(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PatchMapping("/private/servicio")
    public ResponseEntity<?> updateServicio(@RequestBody Servicio servicio) {
        try {
            servicioService.actualizarServicio(servicio);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @PostMapping("/private/servicio")
    public ResponseEntity<?> createServicio(@RequestBody Servicio servicio) {
        try {
            System.out.println("servicio: " + servicio);
            Servicio servicioCreado = servicioService.crearServicio(servicio);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", servicioCreado, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    /**
     * Endpoint para realizar la reserva de un servicio.Este método permite a
     * los usuarios realizar reservas de servicios con asignación de empleados
     * de forma aleatoria o manual.
     *
     * También calcula la hora de fin del servicio y valida la disponibilidad
     * del empleado en el horario seleccionado.
     *
     * @param reservacionServicioRequest Objeto que contiene los detalles de la
     * solicitud de reserva.
     * @return Respuesta HTTP que contiene el reporte en formato PDF o un
     * mensaje de error.
     */
    @Operation(summary = "Reserva un servicio",
            description = "Realiza una reserva de servicio asignando un empleado"
            + " aleatoriamente o de manera manual.",
            tags = {"Reservas", "Servicios"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva realizada "
                + "con éxito y reporte generado",
                content = {
                    @Content(mediaType = "application/pdf",
                            schema = @Schema(type = "string", format = "binary"))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta "
                + "o error en la validación de datos",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping("/cliente/reservaServicio")
    public ResponseEntity<?> reservarServicio(@RequestBody ReservacionServicioRequest reservacionServicioRequest) {
        try {
            // Invoca el método de reserva del servicio
            ArchivoDTO data = reservaService.reservaServicio(reservacionServicioRequest);
            // Retorna el archivo PDF en la respuesta
            return ResponseEntity.ok()
                    .headers(data.getHeaders())
                    .body(data.getArchivo());
        } catch (Exception ex) {
            // Devuelve una respuesta con un mensaje de error si ocurre algún problema
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    /*
     * Retorna los empleados asociados a un servicio
     * @param id id del servicio
     * @return empleados asociados al servicio
     */
    @GetMapping("public/servicio/{id}/empleados")
    public ResponseEntity<?> empleadosAsociadosAlServicio(@PathVariable Long id) {
        try {
            System.out.println("Consulta " + id);
            List<Empleado> data = servicioService.empleadosAsociadosAlServicio(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();

        }
    }
}
