/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usac.api.models.Rol;
import usac.api.models.request.RolCreateRequest;
import usac.api.models.request.RolPermisoUpdateRequest;
import usac.api.services.RolService;
import usac.api.services.permisos.ValidadorPermiso;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 *
 * @author luid
 */
@RestController
@RequestMapping("/api/rol")
public class RolController {

    @Autowired
    private RolService rolService;
    @Autowired
    private ValidadorPermiso validadorPermiso;

    @Operation(description = "Crea un nuevo rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol creado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/private/restricted/crearRol")
    public ResponseEntity<?> crearRol(@RequestBody RolCreateRequest crear) {
        try {
            this.validadorPermiso.verificarPermiso();
            Rol respuesta = rolService.crearRol(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Actualiza un rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PatchMapping("/private/restricted/actualizarRol")
    public ResponseEntity<?> actualizarRol(@RequestBody Rol update) {
        try {
            this.validadorPermiso.verificarPermiso();
            Rol respuesta = rolService.actualizarRol(update);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Actualiza los permisos de un rol en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/private/restricted/actualizarPermisosRol")
    public ResponseEntity<?> actualizarPermisosRol(@RequestBody RolPermisoUpdateRequest update) {
        try {
            this.validadorPermiso.verificarPermiso();
            Rol respuesta = rolService.actualizarPermisosRol(update);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Obtiene un rol en el sistema mediante su Id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))}),
        @ApiResponse(responseCode = "400", description = "Rol no encontrado o solicitud incorrecta.")
    })
    @GetMapping("/private/restricted/getRolById")
    public ResponseEntity<?> getRolById(@PathVariable Long id) {
        try {
            Rol respuesta = rolService.getRolById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Obtiene un rol en el sistema mediante su nombre.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Rol.class))}),
        @ApiResponse(responseCode = "400", description = "Rol no encontrado o solicitud incorrecta.")
    })
    @GetMapping("/private/no_restricted/getRolByNombre/{nombre}")
    public ResponseEntity<?> getRolByNombre(@PathVariable String nombre) {
        try {
            Rol respuesta = rolService.getRolByNombre(nombre);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
