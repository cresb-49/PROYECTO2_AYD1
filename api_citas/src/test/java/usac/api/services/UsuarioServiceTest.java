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

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("mockedUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // Simular que verificarUsuarioJwt no hace nada usando Spy
        doNothing().when(usuarioService).verificarUsuarioJwt(any(Usuario.class));
    }

    @Test
    void testEnviarMailDeRecuperacion_Success() throws Exception {
        String correo = "usuario@ejemplo.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String resultado = usuarioService.enviarMailDeRecuperacion(correo);

        assertNotNull(usuario.getTokenRecuperacion());
        verify(mailService, times(1)).enviarCorreoDeRecuperacion(
                eq(correo), eq(usuario.getTokenRecuperacion()));
        assertEquals("Te hemos enviado un correo electrónico con las instrucciones para recuperar tu cuenta. Por favor revisa tu bandeja de entrada.", resultado);
    }

    @Test
    void testEnviarMailDeRecuperacion_CorreoVacio() {
        String correo = "";
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });
        assertEquals("Correo vacío.", exception.getMessage());
    }

    @Test
    void testEnviarMailDeRecuperacion_CorreoNoEncontrado() {
        String correo = "usuario@ejemplo.com";
        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });
        assertEquals("No hemos encontrado tu correo electrónico.", exception.getMessage());
    }

    @Test
    void testEnviarMailDeRecuperacion_UsuarioDesactivado() throws Exception {
        String correo = "usuario@ejemplo.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));
        doThrow(new Exception("El usuario está desactivado.")).when(usuarioService).isDesactivated(usuario);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });
        assertEquals("El usuario está desactivado.", exception.getMessage());
    }

    @Test
    void testEnviarMailDeRecuperacion_ActualizacionFalla() throws Exception {
        String correo = "usuario@ejemplo.com";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(correo);
        usuario.setTokenRecuperacion(null);

        when(usuarioRepository.findByEmail(correo)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.enviarMailDeRecuperacion(correo);
        });
        assertEquals("No hemos podido enviar el correo electrónico. Inténtalo más tarde.", exception.getMessage());
    }

    @Test
    void testCambiarPassword_Success() throws Exception {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");


        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEncontrado);

        String resultado = usuarioService.cambiarPassword(usuario);

        assertEquals("Se cambió tu contraseña con éxito.", resultado);
        assertEquals("encryptedPassword", usuarioEncontrado.getPassword());
        verify(usuarioRepository, times(1)).save(usuarioEncontrado);
    }

    @Test
    void testCambiarPassword_InvalidId() {
        // Crear un usuario con un ID inválido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(null);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });
        assertEquals("Id inválido.", exception.getMessage());
    }

    @Test
    void testCambiarPassword_UserNotFound() {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    @Test
    void testCambiarPassword_UpdateFail() throws Exception {
        // Crear un usuario con un ID válido
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });
        assertEquals("No pudimos actualizar tu contraseña, inténtalo más tarde.", exception.getMessage());
    }

    @Test
    void testCambiarPassword_InvalidPassword() throws Exception {
        // Crear un usuario con un ID válido pero sin contraseña
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword(null);

        doThrow(new Exception("Contraseña inválida")).when(usuarioService)
                .validarAtributo(usuario, "password");

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });
        assertEquals("Contraseña inválida", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");
        usuario.setPassword("password");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setEmail("usuario@test.com");
        usuarioEncontrado.setPassword("oldPassword");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(usuarioRepository.existsUsuarioByEmailAndIdNot("usuario@test.com", 1L)).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario result = usuarioService.updateUsuario(usuario);

        assertNotNull(result);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuarioEncontrado.getPassword(), result.getPassword());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testUpdateUsuario_InvalidId() {
        Usuario usuario = new Usuario();
        usuario.setId(null);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("Id inválido.", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_UserNotFound() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_EmailExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setEmail("usuario@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(usuarioRepository.existsUsuarioByEmailAndIdNot("usuario@test.com", 1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("No se editó el usuario usuario@test.com, debido a que ya existe otro usuario con el mismo email.", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_PhoneExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@test.com");
        usuario.setId(1L);
        usuario.setPhone("12345678");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPhone("12345678");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(usuarioRepository.existsUsuarioByPhoneAndIdNot("12345678", 1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("No se editó el usuario usuario@test.com, debido a que ya existe otro usuario con el mismo teléfono.", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_CuiExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@test.com");
        usuario.setId(1L);
        usuario.setCui("12345678901");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setCui("12345678901");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(usuarioRepository.existsUsuarioByCuiAndIdNot("12345678901", 1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("No se editó el usuario usuario@test.com, debido a que ya existe otro usuario con el mismo cui.", exception.getMessage());
    }

    @Test
    void testUpdateUsuario_NitExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@test.com");
        usuario.setId(1L);
        usuario.setNit("12345678901");

        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setNit("12345678901");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));
        when(usuarioRepository.existsUsuarioByNitAndIdNot("12345678901", 1L)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.updateUsuario(usuario);
        });
        assertEquals("No se editó el usuario usuario@test.com, debido a que ya existe otro usuario con el mismo nit.", exception.getMessage());
    }

    @Test
    void testIniciarSesion_Success() throws Exception {
        Usuario log = new Usuario();
        log.setEmail("test@test.com");
        log.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.of(usuario));
        when(authenticationService.loadUserByUsername(log.getEmail())).thenReturn(mock(UserDetails.class));
        when(jwtGenerator.generateToken(any(UserDetails.class))).thenReturn("mocked-jwt");

        authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class));

        LoginDTO result = usuarioService.iniciarSesion(log);

        assertNotNull(result);
        assertEquals("mocked-jwt", result.getJwt());
        assertEquals(log.getEmail(), result.getUsuario().getEmail());
    }

    @Test
    void testIniciarSesion_EmailIncorrecto() {
        Usuario log = new Usuario();
        log.setEmail("wrong@test.com");
        log.setPassword("password");

        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.iniciarSesion(log);
        });
        assertEquals("Correo electrónico incorrecto.", exception.getMessage());
    }

    @Test
    void testIniciarSesion_AuthenticationFail() throws Exception {
        Usuario log = new Usuario();
        log.setEmail("test@test.com");
        log.setPassword("password");

        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");

        when(usuarioRepository.findByEmail(log.getEmail())).thenReturn(Optional.of(usuario));
        doThrow(new AuthenticationException("Authentication failed") {
        }).when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.iniciarSesion(log);
        });
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void testRecuperarPassword_Success() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("valid-code");
        request.setNuevaPassword("newpassword");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTokenRecuperacion("valid-code");

        when(usuarioRepository.findByTokenRecuperacion("valid-code")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String result = usuarioService.recuperarPassword(request);

        assertEquals("Se cambió tu contraseña con éxito.", result);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testRecuperarPassword_CodigoInvalido() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("invalid-code");
        request.setNuevaPassword("newpassword");

        when(usuarioRepository.findByTokenRecuperacion("invalid-code"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.recuperarPassword(request);
        });
        assertEquals("Código de autorización inválido.", exception.getMessage());
    }

    @Test
    void testRecuperarPassword_UpdateFail() throws Exception {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("valid-code");
        request.setNuevaPassword("newpassword");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setTokenRecuperacion("valid-code");

        when(usuarioRepository.findByTokenRecuperacion("valid-code"))
                .thenReturn(Optional.of(usuario));

        Usuario updatedUser = new Usuario();
        updatedUser.setId(2L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(updatedUser);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.recuperarPassword(request);
        });
        assertEquals("No pudimos actualizar tu contraseña, inténtalo más tarde.", exception.getMessage());
    }

    @Test
    void testGetUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario user1 = new Usuario();
        user1.setEmail("test1@test.com");
        usuarios.add(user1);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.getUsuarios();

        assertEquals(1, result.size());
        assertEquals("test1@test.com", result.get(0).getEmail());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testGetByEmail_UserExists() throws Exception {
        Usuario user = new Usuario();
        user.setEmail("test@test.com");

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        Usuario result = usuarioService.getByEmail("test@test.com");

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void testGetByEmail_UserNotFound() {
        when(usuarioRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.getByEmail("nonexistent@test.com");
        });
        assertEquals("Usuario no encontrado.", exception.getMessage());
    }

    @Test
    void testGetUsuario_UserExists() throws Exception {
        Usuario user = new Usuario();
        user.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));

        Usuario result = usuarioService.getUsuario(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetUsuario_UserNotFound() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.getUsuario(1L);
        });
        assertEquals("No hemos encontrado el usuario.", exception.getMessage());
    }

    @Test
    void testCrearUsuarioCliente_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNit("12345678901");
        usuario.setCui("12345678901");
        usuario.setPhone("12345678");
        usuario.setEmail("newuser@test.com");
        usuario.setNombres("Nuevo");
        usuario.setApellidos("Usuario");
        usuario.setPassword("password_valida");

        Rol clienteRol = new Rol();
        clienteRol.setNombre("CLIENTE");

        Usuario userCreado = new Usuario();
        userCreado.setId(1L);
        userCreado.setEmail("newuser@test.com");

        when(rolService.getRolByNombre("CLIENTE")).thenReturn(clienteRol);
        when(usuarioRepository.existsByEmail("newuser@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(userCreado);
        when(authenticationService.loadUserByUsername("newuser@test.com")).thenReturn(mock(UserDetails.class));
        when(jwtGenerator.generateToken(any(UserDetails.class))).thenReturn("mocked-jwt");

        LoginDTO result = usuarioService.crearUsuarioCliente(usuario);

        assertNotNull(result);
        assertEquals("mocked-jwt", result.getJwt());
        assertEquals(1L, result.getUsuario().getId());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testCrearUsuarioCliente_ValidationFail() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("");
        usuario.setPassword("password");

        Rol clienteRol = new Rol();
        clienteRol.setNombre("CLIENTE");

        when(rolService.getRolByNombre("CLIENTE")).thenReturn(clienteRol);

        Set<ConstraintViolation<Usuario>> violations = new HashSet<>();
        ConstraintViolation<Usuario> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("El email no puede estar vacío.");
        violations.add(violation);

        when(validator.validate(any(Usuario.class))).thenReturn(violations);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearUsuarioCliente(usuario);
        });

        assertTrue(exception.getMessage().contains("El email no puede estar vacío."));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void testGuardarUsuario_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("existing@test.com");

        when(usuarioRepository.existsByEmail("existing@test.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearUsuarioCliente(usuario);
        });
        assertEquals("El Email ya existe.", exception.getMessage());
    }

    @Test
    void testCrearAdministrador_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@test.com");
        usuario.setPassword("adminpassword");

        Rol adminRol = new Rol();
        adminRol.setNombre("ADMIN");

        when(rolService.getRolByNombre("ADMIN")).thenReturn(adminRol);
        when(usuarioRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(encriptador.encriptar("adminpassword")).thenReturn("encryptedpassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioCreado = usuarioService.crearAdministrador(usuario);

        assertNotNull(usuarioCreado);
        assertEquals("admin@test.com", usuarioCreado.getEmail());
        verify(rolService, times(1)).getRolByNombre("ADMIN");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearEmpleado_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("empleado@test.com");
        usuario.setPassword("empleadopassword");

        Rol empleadoRol = new Rol();
        empleadoRol.setNombre("EMPLEADO");

        when(rolService.getRolByNombre("EMPLEADO")).thenReturn(empleadoRol);
        when(usuarioRepository.existsByEmail("empleado@test.com")).thenReturn(false);
        when(encriptador.encriptar("empleadopassword")).thenReturn("encryptedpassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioCreado = usuarioService.crearEmpleado(usuario);

        assertNotNull(usuarioCreado);
        assertEquals("empleado@test.com", usuarioCreado.getEmail());
        verify(rolService, times(1)).getRolByNombre("EMPLEADO");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearAdministrador_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@test.com");
        usuario.setPassword("adminpassword");

        when(usuarioRepository.existsByEmail("admin@test.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearAdministrador(usuario);
        });
        assertEquals("El Email ya existe.", exception.getMessage());
    }

    @Test
    void testCrearEmpleado_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("empleado@test.com");
        usuario.setPassword("empleadopassword");

        when(usuarioRepository.existsByEmail("empleado@test.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearEmpleado(usuario);
        });
        assertEquals("El Email ya existe.", exception.getMessage());
    }
}
