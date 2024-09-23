/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import usac.api.models.Rol;
import usac.api.models.Usuario;
import usac.api.models.dto.LoginDTO;
import usac.api.repositories.UsuarioRepository;
import usac.api.services.authentification.AuthenticationService;
import usac.api.services.authentification.JwtGeneratorService;

/**
 * Clase de pruebas unitarias para el servicio UsuarioService.
 */
public class UsuarioServiceTest {

    // Inyecta el objeto de UsuarioService en los tests
    @InjectMocks
    private UsuarioService usuarioService;

    // Mock de dependencias externas
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolService rolService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtGeneratorService jwtGenerator;

    @Mock
    private Validator validator;

    /**
     * Método para inicializar los mocks antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks antes de cada test
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
        usuario.setNombre("Nuevo Usuario");
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
