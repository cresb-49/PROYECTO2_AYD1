/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.controllers;

import java.util.List;
import java.util.Map;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.dto.UserChangePasswordDTO;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.services.UsuarioService;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 *
 * @author Luis Monterroso
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Obtener usuario por ID",
            description = "Obtiene la información del usuario basado en el ID proporcionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/private/getUsuario/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id) {
        try {
            Usuario data = usuarioService.getUsuario(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(summary = "Obtener usuario por ID",
            description = "Obtiene la información del usuario basado en el ID proporcionado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/protected/getPerfil/{id}")
    public ResponseEntity<?> getPerfil(@PathVariable Long id) {
        try {
            Usuario data = usuarioService.getPerfil(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(summary = "Enviar correo de recuperación de contraseña",
            description = "Envía un correo de recuperación de contraseña al usuario "
            + "basado en la dirección de correo electrónico proporcionada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/public/mailDeRecupeacion")
    public ResponseEntity<?> mailDeRecupeacion(
            @Parameter(description = "ID del producto a buscar", required = true, example = "{correoElectronico:\"xd\"}") @RequestBody Map<String, Object> requestBody) {
        try {
            String correoElectronico = (String) requestBody.get("correoElectronico");
            String mensaje = usuarioService.enviarMailDeRecuperacion(correoElectronico);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", mensaje, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(summary = "Recuperar contraseña",
            description = "Recupera la contraseña del usuario utilizando el"
            + " código de recuperación y nueva contraseña.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña recuperada exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PatchMapping("/public/recuperarPassword")
    public ResponseEntity<?> recuperarPassword(@RequestBody PasswordChangeRequest requestBody) {
        try {
            String respuesta = usuarioService.recuperarPassword(requestBody);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(summary = "Cambiar contraseña",
            description = "Permite al usuario cambiar su contraseña actual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contraseña cambiada exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PatchMapping("/protected/cambiarPassword")
    public ResponseEntity<?> cambiarPassword(
            @Parameter(description = "ID del producto a buscar", required = true, example = "{id:1,password:\"xd\",newPassword:\"xdnt\"}") @RequestBody UserChangePasswordDTO requestBody) {
        try {
            String respuesta = usuarioService.cambiarPassword(requestBody);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(summary = "Actualizar usuario parcialmente",
            description = "Actualiza parcialmente (sin la password) la información del usuario "
            + "basado en los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "ID con formato inválido")
    })
    @PatchMapping("/protected/actualizarUsuarioSinPassword")
    public ResponseEntity<?> actualizarUsuarioSinPassword(@RequestBody Usuario updates) {
        try {
            Usuario confirmacion = usuarioService.updateUsuario(updates);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", confirmacion, null, null).sendResponse();
        } catch (NumberFormatException ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST,
                    "Id con formato invalido",
                    null, null, ex.getMessage()).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Obtiene la información del todos los usuarios.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/private/getUsuarios")
    public ResponseEntity<?> getUsuarios() {
        try {
            List<Usuario> data = usuarioService.getUsuarios();
            return new ApiBaseTransformer(HttpStatus.OK, "OK", data, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/public/crearCliente")
    public ResponseEntity<?> crearCliente(@RequestBody Usuario crear) {
        try {
            LoginDTO respuesta = usuarioService.crearUsuarioCliente(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Deroga al servicio hacer la autenticacion de usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Credenciales correctas", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/public/login")
    public ResponseEntity<?> login(@RequestBody Usuario crear) {
        try {
            LoginDTO respuesta = usuarioService.iniciarSesion(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
