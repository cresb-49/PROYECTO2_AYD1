package usac.api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import usac.api.models.RolUsuario;

import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.models.request.NuevoEmpleadoRequest;
import usac.api.models.request.PasswordChangeRequest;
import usac.api.models.request.UserChangePasswordRequest;
import usac.api.models.request.UsuarioRolAsignacionRequest;
import usac.api.repositories.RolRepository;
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

    @Mock
    private RolRepository rolRepository;

    // Mock de dependencias externas
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmpleadoService empleadoService;

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

    /**
     * Prueba para asignar roles al usuario, verificando que los roles sean
     * actualizados correctamente.
     */
    @Test
    void testUpdateRoles_Success() throws Exception {
        // Crear usuario local dentro del método e inicializar la lista de roles
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("user@example.com");
        usuarioMock.setRoles(new ArrayList<>()); // Inicializar la lista de roles

        // Crear roles
        Rol rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombre("ADMIN");

        Rol rolEmpleado = new Rol();
        rolEmpleado.setId(2L);
        rolEmpleado.setNombre("EMPLEADO");

        // Asignar rol base ADMIN al usuario
        RolUsuario rolUsuarioAdmin = new RolUsuario(usuarioMock, rolAdmin);
        usuarioMock.getRoles().add(rolUsuarioAdmin);

        // Mock de solicitud de asignación de roles
        UsuarioRolAsignacionRequest asignacionRequest = new UsuarioRolAsignacionRequest();
        asignacionRequest.setUsuarioId(1L);
        asignacionRequest.setRoles(List.of(rolEmpleado));

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Simular búsqueda del rol "EMPLEADO"
        when(rolRepository.findOneByNombre("EMPLEADO")).thenReturn(Optional.of(rolEmpleado));
        when(rolRepository.findById(rolEmpleado.getId())).thenReturn(Optional.of(rolEmpleado));

        // Simular validaciones exitosas
        when(validator.validate(any())).thenReturn(new HashSet<>());
        when(usuarioRepository.save(usuarioMock)).thenReturn(usuarioMock);

        // Ejecutar el método para actualizar roles
        Usuario usuarioActualizado = usuarioService.updateRoles(asignacionRequest);

        // Verificar que el usuario fue actualizado correctamente con los nuevos roles
        assertNotNull(usuarioActualizado);
        assertEquals(1, usuarioActualizado.getRoles().size()); // ADMIN + EMPLEADO
        assertTrue(usuarioActualizado.getRoles().stream().map(RolUsuario::getRol).collect(Collectors.toSet()).contains(rolEmpleado));

        verify(usuarioRepository, times(1)).save(usuarioMock);
    }

    /**
     * Prueba para verificar que se lanza una excepción cuando el usuario no
     * tiene un rol base.
     */
    @Test
    void testUpdateRoles_NoBaseRole() throws Exception {
        // Crear usuario local dentro del método e inicializar la lista de roles
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("user@example.com");
        usuarioMock.setRoles(new ArrayList<>()); // Inicializar la lista de roles

        // Mock de solicitud de asignación de roles
        UsuarioRolAsignacionRequest asignacionRequest = new UsuarioRolAsignacionRequest();
        asignacionRequest.setUsuarioId(1L);

        ArrayList<Rol> rolesNuevos = new ArrayList<>();
        rolesNuevos.add(new Rol("ADMIN"));
        asignacionRequest.setRoles(rolesNuevos); // No tiene roles asignados

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Ejecutar el método y verificar que se lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> usuarioService.updateRoles(asignacionRequest));

        // Verificar el mensaje de la excepción
        assertEquals("El usuario no tiene un rol base asignado", exception.getMessage());
    }

    /**
     * Prueba para verificar que se lanza una excepción cuando se intenta
     * asignar el rol "CLIENTE".
     */
    @Test
    void testUpdateRoles_CannotAssignClienteRole() throws Exception {
        // Crear usuario local dentro del método e inicializar la lista de roles
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("user@example.com");
        usuarioMock.setRoles(new ArrayList<>()); // Inicializar la lista de roles
        // Crear roles
        Rol rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombre("ADMIN");
        // Asignar rol base ADMIN al usuario
        RolUsuario rolUsuarioAdmin = new RolUsuario(usuarioMock, rolAdmin);
        usuarioMock.getRoles().add(rolUsuarioAdmin);

        // Crear rol cliente
        Rol rolEmpleado = new Rol();
        rolEmpleado.setId(3L);
        rolEmpleado.setNombre("EMPLEADO");

        // Crear rol cliente
        Rol rolCliente = new Rol();
        rolCliente.setId(3L);
        rolCliente.setNombre("CLIENTE");

        // Mock de solicitud de asignación de roles con el rol "CLIENTE"
        UsuarioRolAsignacionRequest asignacionRequest = new UsuarioRolAsignacionRequest();
        asignacionRequest.setUsuarioId(1L);
        asignacionRequest.setRoles(List.of(rolCliente));

        when(rolRepository.findOneByNombre("EMPLEADO")).thenReturn(Optional.of(rolEmpleado));

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Simular búsqueda del rol "CLIENTE"
        when(rolRepository.findById(rolCliente.getId())).thenReturn(Optional.of(rolCliente));

        // Ejecutar el método y verificar que se lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> usuarioService.updateRoles(asignacionRequest));

        // Verificar el mensaje de la excepción
        assertEquals("No es posible asignar el rol 'CLIENTE'.", exception.getMessage());
    }

    /**
     * Prueba para verificar que el rol "ADMIN" no se pueda asignar cuando ya
     * está asignado.
     */
    @Test
    void testUpdateRoles_CannotDuplicateAdminRole() throws Exception {
        // Crear usuario local dentro del método e inicializar la lista de roles
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setEmail("user@example.com");
        usuarioMock.setRoles(new ArrayList<>()); // Inicializar la lista de roles

        // Crear rol Admin
        Rol rolAdmin = new Rol();
        rolAdmin.setId(1L);
        rolAdmin.setNombre("ADMIN");

        Rol rolEmpleado = new Rol();
        rolEmpleado.setId(2L);
        rolEmpleado.setNombre("EMPLEADO");

        // Asignar rol base ADMIN al usuario
        RolUsuario rolUsuarioAdmin = new RolUsuario(usuarioMock, rolAdmin);
        usuarioMock.getRoles().add(rolUsuarioAdmin);

        // Mock de solicitud de asignación de roles con el rol "ADMIN"
        UsuarioRolAsignacionRequest asignacionRequest = new UsuarioRolAsignacionRequest();
        asignacionRequest.setUsuarioId(1L);
        asignacionRequest.setRoles(List.of(rolAdmin)); // Intentar reasignar "ADMIN"

        when(rolRepository.findOneByNombre("ADMIN")).thenReturn(Optional.of(rolAdmin));

        // Simular búsqueda del rol "EMPLEADO"
        when(rolRepository.findOneByNombre("EMPLEADO")).thenReturn(Optional.of(rolEmpleado));

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioMock));

        // Simular búsqueda del rol "ADMIN"
        when(rolRepository.findById(rolAdmin.getId())).thenReturn(Optional.of(rolAdmin));

        when(usuarioRepository.save(usuarioMock)).thenReturn(usuarioMock);

        // Ejecutar el método y verificar que no se duplique el rol "ADMIN"
        Usuario usuarioActualizado = usuarioService.updateRoles(asignacionRequest);

        // Verificar que el rol "ADMIN" no fue duplicado
        assertNotNull(usuarioActualizado);
        assertEquals(1, usuarioActualizado.getRoles().size()); // Solo debe tener el rol "ADMIN"
        assertTrue(usuarioActualizado.getRoles().stream().map(RolUsuario::getRol).anyMatch(rol -> rol.getNombre().equals("ADMIN")));

        verify(usuarioRepository, times(1)).save(usuarioMock);
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
        // Crear una solicitud de cambio de contraseña con datos válidos
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        // Simulación del usuario encontrado en la base de datos
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        when(encriptador.compararPassword(usuario.getPassword(),
                usuarioEncontrado.getPassword())).
                thenReturn(Boolean.TRUE);

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        // Simular encriptado de la nueva contraseña
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");

        // Simular que la actualización del usuario es exitosa
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEncontrado);

        // Ejecutar el método de cambio de contraseña
        String resultado = usuarioService.cambiarPassword(usuario);

        // Verificación del resultado y del encriptado
        assertEquals("Se cambió tu contraseña con éxito.", resultado);
        assertEquals("encryptedPassword", usuarioEncontrado.getPassword());
        verify(usuarioRepository, times(1)).save(usuarioEncontrado);
    }

    @Test
    void testCambiarPassword_InvalidId() {
        // Crear un usuario con un ID inválido (null)
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(null);  // ID inválido
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = new HashSet<>();
        ConstraintViolation<UserChangePasswordRequest> violation = mock(
                ConstraintViolation.class);

        when(violation.getMessage()).thenReturn("El id no puede ser nulo.");
        violations.add(violation);
        when(validator.validate(any(UserChangePasswordRequest.class))).thenReturn(violations);

        // Verificamos que se lanza una excepción debido a la validación del ID
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar que el mensaje de la excepción sea por el ID nulo
        assertEquals("El id no puede ser nulo.\n", exception.getMessage());
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
        // Crear una solicitud de cambio de contraseña con datos válidos
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);
        usuario.setPassword("oldPassword");
        usuario.setNewPassword("newPassword");

        // Simulación del usuario encontrado en la base de datos
        Usuario usuarioEncontrado = new Usuario();
        usuarioEncontrado.setId(1L);
        usuarioEncontrado.setPassword("oldPassword");

        // Simular búsqueda del usuario por ID
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEncontrado));

        when(encriptador.compararPassword(usuario.getPassword(),
                usuarioEncontrado.getPassword())).
                thenReturn(Boolean.TRUE);
        // Simular el encriptado de la nueva contraseña
        when(encriptador.encriptar("newPassword")).thenReturn("encryptedPassword");

        // Simular que la actualización falla (devuelve null)
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(null);

        // Verificar que lanza una excepción
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificación del mensaje de la excepción
        assertEquals("No pudimos actualizar tu contraseña, inténtalo más tarde.", exception.getMessage());
    }

    @Test
    void testCambiarPassword_InvalidPassword() {
        // Crear un usuario con una contraseña nueva inválida (null)
        UserChangePasswordRequest usuario = new UserChangePasswordRequest();
        usuario.setId(1L);  // ID válido
        usuario.setPassword("oldPassword");
        usuario.setNewPassword(null);  // Contraseña nueva inválida

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = new HashSet<>();
        ConstraintViolation<UserChangePasswordRequest> violation = mock(
                ConstraintViolation.class);

        when(violation.getMessage()).thenReturn("La contraseña nueva no puede ser nula.");
        violations.add(violation);
        when(validator.validate(any(UserChangePasswordRequest.class))).thenReturn(violations);

        // Verificamos que se lanza una excepción debido a la validación de la contraseña nueva
        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.cambiarPassword(usuario);
        });

        // Verificar que el mensaje de la excepción sea por la contraseña nueva nula
        assertEquals("La contraseña nueva no puede ser nula.\n", exception.getMessage());
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
    /*
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
        assertEquals(String.format("El Email %s ya existe.", usuario.getEmail()), exception.getMessage());
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
        empleadoRol.setId(1L);
        empleadoRol.setNombre("EMPLEADO");

        when(rolService.getRolByNombre("EMPLEADO")).thenReturn(empleadoRol);
        when(usuarioRepository.existsByEmail("empleado@test.com")).thenReturn(false);
        when(encriptador.encriptar("empleadopassword")).thenReturn("encryptedpassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioCreado = usuarioService.crearEmpleado(new NuevoEmpleadoRequest(usuario, new ArrayList<>(), empleadoRol));

        assertEquals("empleado@test.com", usuarioCreado.getEmail());
        verify(rolService, times(1)).getRolByNombre("EMPLEADO");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void testCrearAdministrador_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@test.com");
        usuario.setPassword("adminpassword");
        usuario.setId(1L);

        when(usuarioRepository.existsByEmail("admin@test.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearAdministrador(usuario);
        });
        assertEquals(String.format("El Email %s ya existe.", usuario.getEmail()), exception.getMessage());
    }

    @Test
    void testCrearEmpleado_EmailAlreadyExists() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("empleado@test.com");
        usuario.setPassword("empleadopassword");

        Rol rol = new Rol();
        rol.setId(1L);

        when(usuarioRepository.existsByEmail("empleado@test.com")).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            usuarioService.crearEmpleado(new NuevoEmpleadoRequest(usuario, new ArrayList<>(), rol));
        });
        assertEquals(String.format("El Email %s ya existe.", usuario.getEmail()), exception.getMessage());
    }
     */
}
