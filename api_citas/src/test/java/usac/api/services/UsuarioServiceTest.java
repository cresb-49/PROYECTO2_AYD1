/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import usac.api.models.Rol;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.models.request.UserChangePasswordRequest;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.authentification.AuthenticationService;
import usac.api.services.authentification.JwtGeneratorService;
import usac.api.tools.Encriptador;

/**
 * Clase de pruebas unitarias para el servicio UsuarioService.
 */
public class UsuarioServiceTest {

    // Inyecta el objeto de UsuarioService en los tests
    @Spy  // Usa Spy para la clase concreta
    @InjectMocks
    private UsuarioService usuarioService;

    // Mock de dependencias externas
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Encriptador encriptador;

    @Mock
    private RolService rolService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtGeneratorService jwtGenerator;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Validator validator;

    @Mock
    private MailService mailService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mockear el contexto de seguridad de Spring Security
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("mockedUser");  // Simula el nombre de usuario autenticado
        when(authentication.getPrincipal()).thenReturn(userDetails);  // Simula el objeto UserDetails

        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // Simular que verificarUsuarioJwt no hace nada usando Spy
        doNothing().when(usuarioService).verificarUsuarioJwt(any(Usuario.class));
    }

    /**
     * Prueba exitosa de envío de correo de recuperación.
     */
    @Test
    void testEnviarMailDeRecuperacion_Success() throws Exception {
        // Simulación de un correo válido
        String correo = "usuario@ejemplo.com";

        // Crear un usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        // Simular la búsqueda del usuario por correo
        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));

        // Simular la actualización del usuario
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Llamar al método y obtener el resultado
        String resultado = usuarioService.enviarMailDeRecuperacion(correo);

        // Verificar que el código de recuperación fue generado y que se envió el correo
        assertNotNull(usuario.getTokenRecuperacion());
        verify(mailService, times(1)).enviarCorreoEnSegundoPlano(
                eq(correo), eq(usuario.getTokenRecuperacion()), eq(2));
        assertEquals("Te hemos enviado un correo electrónico con las instrucciones para recuperar tu cuenta. Por favor revisa tu bandeja de entrada.", resultado);
    }

    /**
     * Prueba que lanza excepción cuando el correo está vacío.
     */
    @Test
    void testEnviarMailDeRecuperacion_CorreoVacio() {
        // Simulación de un correo vacío
        String correo = "";

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Correo vacío.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando el correo no es encontrado.
     */
    @Test
    void testEnviarMailDeRecuperacion_CorreoNoEncontrado() {
        // Simulación de un correo válido pero no encontrado
        String correo = "usuario@ejemplo.com";

        // Simular que el usuario no es encontrado
        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.empty());

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });

        // Verificar el mensaje de la excepción
        assertEquals("No hemos encontrado tu correo electrónico.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando el usuario está desactivado.
     */
    @Test
    void testEnviarMailDeRecuperacion_UsuarioDesactivado() throws Exception {
        // Simulación de un correo válido
        String correo = "usuario@ejemplo.com";

        // Crear un usuario simulado y desactivado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        // Simular la búsqueda del usuario
        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));

        // Simular que el usuario está desactivado
        doThrow(new Exception("El usuario está desactivado.")).when(usuarioService).isDesactivated(usuario);

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });

        // Verificar el mensaje de la excepción
        assertEquals("El usuario está desactivado.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando la actualización del usuario falla.
     */
    @Test
    void testEnviarMailDeRecuperacion_ActualizacionFalla() throws Exception {
        // Simulación de un correo válido
        String correo = "usuario@ejemplo.com";

        // Crear un usuario simulado
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        // Simular la búsqueda del usuario
        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));

        // Simular un fallo al actualizar el usuario (retornar null)
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });

        // Verificar el mensaje de la excepción
        assertEquals("No hemos podido enviar el correo electrónico. Inténtalo más tarde.", exception.getMessage());
    }

    /**
     * Prueba exitosa de cambio de contraseña.
     */
    @Test
    void testCambiarPassword_Success() throws Exception {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");


        // Simulación de usuario encontrado en la base de datos
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        // Simular la búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular la encriptación de la nueva contraseña
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");

        // Simular la actualización del usuario
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEncontrado);

        // Llamar al método a probar
        String resultado = usuarioService.cambiarPassword(usuario);

        // Verificaciones
        assertEquals("Se cambió tu contraseña con éxito.", resultado);
        assertEquals("encryptedPassword", usuarioEncontrado.getPassword());
        verify(usuarioRepository, times(1)).save(usuarioEncontrado);
    }

    /**
     * Prueba que lanza excepción cuando el ID es inválido.
     */
    @Test
    void testCambiarPassword_InvalidId() {
        // Crear un usuario con un ID inválido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(null);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Id inválido.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando no se encuentra el usuario.
     */
    @Test
    void testCambiarPassword_UserNotFound() {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        // Simular que no se encuentra el usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar el mensaje de la excepción
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando la actualización falla.
     */
    @Test
    void testCambiarPassword_UpdateFail() throws Exception {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        // Simulación de usuario encontrado en la base de datos
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        // Simular la búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular la encriptación de la nueva contraseña
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");

        // Simular un fallo al guardar el usuario (retornar null)
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar el mensaje de la excepción
        assertEquals("No pudimos actualizar tu contraseña, inténtalo más tarde.", exception.getMessage());
    }

    /**
     * Prueba que verifica la validación de la contraseña.
     */
    @Test
    void testCambiarPassword_InvalidPassword() throws Exception {
        // Crear un usuario con un ID válido pero sin contraseña
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword(null);

        // Simular que la contraseña no es válida
        doThrow(new Exception("Contraseña inválida")).when(usuarioService)
                .validarAtributo(usuario, "password");

        // Llamar al método y verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar el mensaje de la excepción
        assertEquals("Contraseña inválida", exception.getMessage());
    }

    /**
     * Prueba exitosa de actualización de usuario.
     */
    @Test
    void testUpdateUsuario_Success() throws Exception {
        // Usuario con datos válidos
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");
        usuario.setPassword("password");

        // Usuario encontrado en la base de datos
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setEmail("usuario@test.com");
        usuarioEncontrado.setPassword("oldPassword");

        // Simulación de búsqueda del usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular que no existe otro usuario con el mismo email
        when(usuarioRepository.existsUsuarioByEmailAndIdNot("usuario@test.com", 1L)).thenReturn(false);

        // Simular la actualización exitosa
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Llamada al método que estamos probando
        Usuario result = usuarioService.updateUsuario(usuario);

        // Verificaciones
        assertNotNull(result);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuarioEncontrado.getPassword(), result.getPassword()); // La contraseña debe mantenerse
        verify(usuarioRepository, times(1)).save(usuario);
    }

    /**
     * Prueba que lanza excepción por ID inválido.
     */
    @Test
    void testUpdateUsuario_InvalidId() {
        // Usuario con ID inválido
        Usuario usuario = new Usuario();
        usuario.setId(null);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        // Verificación del mensaje de error
        assertEquals("Id inválido.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando el usuario no es encontrado.
     */
    @Test
    void testUpdateUsuario_UserNotFound() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        // Simular que el usuario no fue encontrado en la base de datos
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        // Verificación del mensaje de error
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando ya existe otro usuario con el mismo
     * email.
     */
    @Test
    void testUpdateUsuario_EmailExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setEmail("usuario@test.com");

        // Simular que el usuario fue encontrado
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular que ya existe otro usuario con el mismo email
        when(usuarioRepository.existsUsuarioByEmailAndIdNot("usuario@test.com", 1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        // Verificación del mensaje de error
        assertEquals("No se editó el usuario usuario@test.com, debido a que ya existe otro usuario con el mismo email.", exception.getMessage());
    }

    /**
     * Prueba que lanza excepción cuando la actualización del usuario falla.
     */
    @Test
    void testUpdateUsuario_UpdateFail() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setEmail("usuario@test.com");

        // Simular que el usuario fue encontrado
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular que no existe otro usuario con el mismo email
        when(usuarioRepository.existsUsuarioByEmailAndIdNot("usuario@test.com", 1L)).thenReturn(false);

        // Simular que la actualización falla
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(
                null);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });

        // Verificación del mensaje de error
        assertEquals("No pudimos actualizar el usuario, inténtalo más tarde.", exception.getMessage());
    }

    @Test
    void testIniciarSesion_Success() throws Exception {
        Usuario log = new Usuario();
        log.setEmail("test@test.com");
        log.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        // Simulaciones de dependencias
        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationService.loadUserByUsername(log.getEmail())).thenReturn(mock(UserDetails.class));
        when(jwtGenerator.generateToken(any(UserDetails.class))).thenReturn("mocked-jwt");

        // No necesitas usar doNothing() para la autenticación exitosa, solo deja la simulación como está
        authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Llamada al método
        LoginDTO result = usuarioService.iniciarSesion(log);

        // Verificaciones
        assertNotNull(result);
        assertEquals("mocked-jwt", result.getJwt());
        assertEquals(log.getEmail(), result.getUsuario().getEmail());
    }

    /**
     * Prueba que verifica que se lanza una excepción cuando el correo es
     * incorrecto.
     */
    @Test
    void testIniciarSesion_EmailIncorrecto() {
        Usuario log = new Usuario();
        log.setEmail("wrong@test.com");
        log.setPassword("password");

        // Simular que el usuario no existe
        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.iniciarSesion(log);
        });

        // Verificación
        assertEquals("Correo electrónico incorrecto.", exception.getMessage());
    }

    /**
     * Prueba que verifica que se lanza una excepción cuando la autenticación
     * falla.
     */
    @Test
    void testIniciarSesion_AuthenticationFail() throws Exception {
        Usuario log = new Usuario();
        log.setEmail("test@test.com");
        log.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        // Simular la existencia del usuario
        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.of(usuario));

        // Simular fallo de autenticación
        doThrow(new AuthenticationException("Authentication failed") {
        }).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.iniciarSesion(log);
        });

        // Verificación
        assertEquals("Authentication failed", exception.getMessage());
    }

    /**
     * Prueba exitosa para el método recuperarPassword.
     */
    @Test
    void testRecuperarPassword_Success() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("valid-code");
        request.setNuevaPassword("newpassword");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTokenRecuperacion("valid-code");

        // Simular que el código de recuperación es válido
        when(usuarioRepository.findByTokenRecuperacion("valid-code")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Llamar al método
        String result = usuarioService.recuperarPassword(request);

        // Verificar el éxito
        assertEquals("Se cambió tu contraseña con éxito.", result);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    /**
     * Prueba que verifica que se lanza una excepción cuando el código de
     * recuperación es inválido.
     */
    @Test
    void testRecuperarPassword_CodigoInvalido() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("invalid-code");
        request.setNuevaPassword("newpassword");

        // Simular que no se encuentra el código de recuperación
        when(usuarioRepository.findByTokenRecuperacion("invalid-code"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.recuperarPassword(request);
        });

        // Verificación
        assertEquals("Código de autorización inválido.", exception.getMessage());
    }

    /**
     * Prueba que verifica que se lanza una excepción cuando la actualización de
     * la contraseña falla.
     */
    @Test
    void testRecuperarPassword_UpdateFail() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("valid-code");
        request.setNuevaPassword("newpassword");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTokenRecuperacion("valid-code");

        // Simular que el código de recuperación es válido
        when(usuarioRepository.findByTokenRecuperacion("valid-code"))
                .thenReturn(Optional.of(usuario));

        // Simular fallo al guardar la nueva contraseña
        Usuario updatedUser = new Usuario();
        updatedUser.setId(2L); // Simulamos un error en la actualización
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedUser);

        // Verificar la excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.recuperarPassword(request);
        });

        assertEquals("No pudimos actualizar tu contraseña, inténtalo más tarde.",
                exception.getMessage());
    }

    /**
     * Prueba que verifica que se obtienen todos los usuarios no eliminados
     * correctamente.
     */
    @Test
    void testGetUsuarios() {
        // Crear lista simulada de usuarios
        List<Usuario> usuarios = new ArrayList<>();
        Usuario user1 = new Usuario();
        user1.setEmail("test1@test.com");
        usuarios.add(user1);

        // Simular la respuesta del repositorio
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Llamada al método del servicio
        List<Usuario> result = usuarioService.getUsuarios();

        // Verificaciones
        assertEquals(1, result.size());  // Verifica que la lista tiene un usuario
        assertEquals("test1@test.com", result.get(0).getEmail());  // Verifica el email del usuario
        verify(usuarioRepository, times(1)).findAll();  // Verifica que se llamó al repositorio
    }

    /**
     * Prueba que verifica que un usuario es encontrado por su correo
     * electrónico.
     */
    @Test
    void testGetByEmail_UserExists() throws Exception {
        Usuario user = new Usuario();
        user.setEmail("test@test.com");

        // Simula que el usuario con el correo existe
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        // Llama al método getByEmail
        Usuario result = usuarioService.getByEmail("test@test.com");

        // Verificaciones
        assertNotNull(result);  // Verifica que el usuario no sea nulo
        assertEquals("test@test.com", result.getEmail());  // Verifica que el correo coincide
    }

    /**
     * Prueba que verifica el lanzamiento de excepción cuando un usuario no es
     * encontrado por correo.
     */
    @Test
    void testGetByEmail_UserNotFound() {
        // Simula que no se encuentra el usuario
        when(usuarioRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.getByEmail("nonexistent@test.com");
        });

        // Verifica el mensaje de la excepción
        assertEquals("Usuario no encontrado.", exception.getMessage());
    }

    /**
     * Prueba que verifica que un usuario es encontrado por su ID.
     */
    @Test
    void testGetUsuario_UserExists() throws Exception {
        Usuario user = new Usuario();
        user.setId(1L);

        // Simula que el usuario con el ID existe
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        // Llama al método getUsuario
        Usuario result = usuarioService.getUsuario(1L);

        // Verificaciones
        assertNotNull(result);  // Verifica que el usuario no sea nulo
        assertEquals(1L, result.getId());  // Verifica que el ID del usuario coincide
    }

    /**
     * Prueba que verifica el lanzamiento de excepción cuando un usuario no es
     * encontrado por ID.
     */
    @Test
    void testGetUsuario_UserNotFound() {
        // Simula que no se encuentra el usuario
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Verifica que se lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.getUsuario(1L);
        });

        // Verifica el mensaje de la excepción
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    @Test
    void testCrearUsuarioCliente_Success() throws Exception {
        // Crear un usuario válido que cumpla con las restricciones de validación
        Usuario usuario = new Usuario();
        usuario.setNit("12345678901");
        usuario.setCui("12345678901");
        usuario.setPhone("12345678");
        usuario.setEmail("newuser@test.com");
        usuario.setNombres("Nuevo");
        usuario.setApellidos("Usuario");
        usuario.setPassword("password_valida");

        // Crear un rol de cliente de prueba
        Rol clienteRol = new Rol();
        clienteRol.setNombre("CLIENTE");

        // Crear un usuario simulado que será retornado por el método guardarUsuario
        Usuario userCreado = new Usuario();
        userCreado.setId(1L);
        userCreado.setEmail("newuser@test.com");

        // Simular la obtención del rol y la existencia del usuario por email
        when(rolService.getRolByNombre("CLIENTE")).thenReturn(clienteRol);
        when(usuarioRepository.existsByEmail("newuser@test.com")).thenReturn(false);

        // Simular el comportamiento del método guardarUsuario
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(userCreado);

        // Simular la carga del usuario para autenticación y generación del JWT
        when(authenticationService.loadUserByUsername("newuser@test.com")).thenReturn(mock(UserDetails.class));
        when(jwtGenerator.generateToken(any(UserDetails.class))).thenReturn("mocked-jwt");

        // Llama al método crearUsuarioCliente
        LoginDTO result = usuarioService.crearUsuarioCliente(usuario);

        // Verificaciones
        assertNotNull(result);  // Verifica que el resultado no es nulo
        assertEquals("mocked-jwt", result.getJwt());  // Verifica que el JWT generado es correcto
        assertEquals(1L, result.getUsuario().getId());  // Verifica que el ID del usuario es correcto
        verify(usuarioRepository, times(1)).save(usuario);  // Verifica que el usuario fue guardado
    }

    /**
     * Prueba que verifica que las validaciones fallan cuando la información es
     * incompleta.
     */
    @Test
    void testCrearUsuarioCliente_ValidationFail() throws Exception {
        // Crear un usuario con información incompleta (faltan campos obligatorios)
        Usuario usuario = new Usuario();
        usuario.setEmail("");  // Email vacío (no válido)
        usuario.setPassword("password");

        // Crear un rol de cliente de prueba
        Rol clienteRol = new Rol();
        clienteRol.setNombre("CLIENTE");

        // Simular la obtención del rol
        when(rolService.getRolByNombre("CLIENTE")).thenReturn(clienteRol);

        // Simular una lista de violaciones de validación
        Set<ConstraintViolation<Usuario>> violations = new HashSet<>();
        ConstraintViolation<Usuario> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("El email no puede estar vacío.");
        violations.add(violation);

        // Mock del validador para devolver las violaciones de validación
        when(validator.validate(any(Usuario.class))).thenReturn(violations);

        // Verificar que el método lanza una excepción debido a la falla de validación
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearUsuarioCliente(usuario);
        });

        // Verificar que el mensaje de la excepción es el esperado
        assertTrue(exception.getMessage().contains("El email no puede estar vacío."));

        // Verificar que el método save no fue llamado debido a las validaciones fallidas
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    /**
     * Prueba que verifica que se lanza una excepción si el correo del usuario
     * ya existe.
     */
    @Test
    void testGuardarUsuario_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("existing@test.com");

        // Simula que el correo del usuario ya existe en el sistema
        when(usuarioRepository.existsByEmail("existing@test.com")).thenReturn(true);

        // Verifica que se lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearUsuarioCliente(usuario);
        });

        // Verifica el mensaje de la excepción
        assertEquals("El Email ya existe.", exception.getMessage());
    }
}
