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
import org.springframework.web.bind.annotation.DeleteMapping;
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
import javax.validation.Valid;
import usac.api.models.Empleado;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.request.CreacionClienteRequest;
import usac.api.models.request.CreateTokenAuthRequest;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.models.request.UpdateEmpleadoRequest;
import usac.api.models.request.UserChangePasswordRequest;
import usac.api.models.request.UsuarioRolAsignacionRequest;
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
            @Parameter(description = "ID del producto a buscar", required = true, example = "{id:1,password:\"xd\",newPassword:\"xdnt\"}") @RequestBody UserChangePasswordRequest requestBody) {
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

    /**
     * Crea un nuevo cliente en el sistema utilizando un token de autenticación.
     *
     * @param crear Objeto con la información del cliente a crear.
     * @return ResponseEntity con la información del usuario creado y el JWT.
     * @throws Exception si ocurre un error durante la creación del usuario.
     */
    @Operation(
            summary = "Crear un nuevo cliente",
            description = "Este endpoint permite crear un nuevo usuario cliente en el sistema."
            + " El usuario debe proporcionar un token de verificación válido,"
            + " junto con sus datos personales como NIT, CUI, teléfono, nombres"
            + " y apellidos. El token se valida antes de crear el usuario,"
            + " y si es válido, se genera un JWT para el nuevo cliente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar crear el usuario.", content = {
            @Content(mediaType = "application/json")})
    })
    @PostMapping("/public/crearCliente")
    public ResponseEntity<?> crearCliente(@RequestBody CreacionClienteRequest crear) {
        try {
            LoginDTO respuesta = usuarioService.crearUsuarioCliente(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    /**
     * Endpoint para enviar un token temporal al correo del usuario para
     * verificar la creación del cliente.
     *
     * @param crear Objeto que contiene el correo electrónico del cliente.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @Operation(summary = "Enviar token de registro", description = "Envía un token temporal al correo proporcionado para verificar la creación del usuario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token enviado exitosamente",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiBaseTransformer.class))),
        @ApiResponse(responseCode = "400", description = "Error en la solicitud o envío del token",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiBaseTransformer.class)))
    })
    @PostMapping("/public/enviarTokenRegistro")
    public ResponseEntity<?> enviarTokenRegistro(@RequestBody CreateTokenAuthRequest crear) {
        try {
            usuarioService.enviarTokenRegistro(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK",
                    "Hemos enviado un correo electronico con tu token.", null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Crea un nuevo usuario adminstrador en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario admin exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/private/crearAdministrador")
    public ResponseEntity<?> crearAdministrador(@RequestBody Usuario crear) {
        try {
            Usuario respuesta = usuarioService.crearAdministrador(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Crea un nuevo usuario adminstrador en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario admin exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping("/private/crearEmpleado")
    public ResponseEntity<?> crearEmpleado(@RequestBody NuevoEmpleadoRequest crear) {
        try {
            Usuario respuesta = usuarioService.crearEmpleado(crear);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Actualiza el empleado en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario admin exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PatchMapping("/private/actualizarEmpleado")
    public ResponseEntity<?> actualizarEmpleado(@RequestBody UpdateEmpleadoRequest actualizar) {
        try {
            Empleado respuesta = usuarioService.actualizarEmpleado(actualizar);
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

    @Operation(description = "Actualiza los roles de un usuario. Si el usuario tiene el rol \"ADMIN\", se\n"
            + "cambia a \"EMPLEADO\" y se asignan los nuevos roles (sin reasignar\n"
            + "\"ADMIN\"). Si el usuario tiene el rol \"EMPLEADO\" y se le asigna el rol\n"
            + "\"ADMIN\", solo se le asigna el rol \"ADMIN\". Si el usuario tiene el rol\n"
            + "\"CLIENTE\", se lanzará un error y no se le asignarán más roles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Actualizacion realizada con exito.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PatchMapping("/private/asignarRolAUsuario")
    public ResponseEntity<?> asignarRolAUsuario(@RequestBody UsuarioRolAsignacionRequest asignar) {
        try {
            Usuario respuesta = usuarioService.updateRoles(asignar);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", respuesta, null, null).sendResponse();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }

    @Operation(description = "Elimina un usuario del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))}),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @DeleteMapping("/private/usuario/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioService.eliminarUsuarioById(id);
            return new ApiBaseTransformer(HttpStatus.OK, "OK", null, null, null).sendResponse();
        } catch (Exception ex) {
            return new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Error", null, null, ex.getMessage()).sendResponse();
        }
    }
}
