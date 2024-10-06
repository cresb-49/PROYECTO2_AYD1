/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import usac.api.models.Auditor;
import usac.api.models.Usuario;

/**
 *
 * @author Luis Monterroso
 */
public class ServiceTest {

    @InjectMocks
    private Service service;

    @Mock
    private Validator validator;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Prueba exitosa para validar un modelo sin errores.
     */
    @Test
    void testValidarModelo_Success() throws Exception {
        Object object = new Object();
        when(validator.validate(object)).thenReturn(Collections.emptySet());

        assertTrue(service.validarModelo(object));
        verify(validator, times(1)).validate(object);
    }

    /**
     * Prueba para validar un modelo con errores, que debe lanzar una excepción.
     */
    @Test
    void testValidarModelo_WithErrors() {
        Object object = new Object();
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Error de validación");
        Set<ConstraintViolation<Object>> violations = new HashSet<>(Collections.singletonList(violation));
        when(validator.validate(object)).thenReturn(violations);

        Exception exception = assertThrows(Exception.class, () -> service.validarModelo(object));
        assertEquals("Error de validación\n", exception.getMessage());
    }

    /**
     * Prueba para ignorar entidades desactivadas correctamente.
     */
    @Test
    void testIgnorarEliminados_Success() {
        Auditor auditorActivo = mock(Auditor.class);
        when(auditorActivo.getDesactivatedAt()).thenReturn(null);

        Auditor auditorInactivo = mock(Auditor.class);
        when(auditorInactivo.getDesactivatedAt()).thenReturn(LocalDateTime.now());

        List<Auditor> lista = Arrays.asList(auditorActivo, auditorInactivo);
        List<Auditor> resultado = service.ignorarEliminados(lista);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(auditorActivo));
    }

    /**
     * Prueba para verificar si una entidad está desactivada y lanza excepción.
     */
    @Test
    void testIsDesactivated_ThrowsException() {
        Auditor auditor = mock(Auditor.class);
        // Simular que el auditor está desactivado con LocalDateTime
        when(auditor.getDesactivatedAt()).thenReturn(LocalDateTime.now());

        // Verificar que se lanza una excepción con el mensaje correcto
        Exception exception = assertThrows(Exception.class, () -> service.isDesactivated(auditor));
        assertEquals("Informacion no encontrada.", exception.getMessage());
    }

    /**
     * Prueba exitosa para verificar una entidad no desactivada.
     */
    @Test
    void testIsDesactivated_Success() throws Exception {
        Auditor auditor = mock(Auditor.class);
        when(auditor.getDesactivatedAt()).thenReturn(null);

        service.isDesactivated(auditor);  // No debe lanzar excepción
    }

    /**
     * Prueba exitosa para validar un atributo de un modelo sin errores.
     */
    @Test
    void testValidarAtributo_Success() throws Exception {
        Object object = new Object();
        String attributeName = "atributo";
        when(validator.validateProperty(object, attributeName)).thenReturn(Collections.emptySet());

        assertTrue(service.validarAtributo(object, attributeName));
        verify(validator, times(1)).validateProperty(object, attributeName);
    }

    /**
     * Prueba para validar un atributo de un modelo con errores.
     */
    @Test
    void testValidarAtributo_WithErrors() {
        Object object = new Object();
        String attributeName = "atributo";
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Error de validación");
        Set<ConstraintViolation<Object>> violations = new HashSet<>(Collections.singletonList(violation));
        when(validator.validateProperty(object, attributeName)).thenReturn(violations);

        Exception exception = assertThrows(Exception.class, () -> service.validarAtributo(object, attributeName));
        assertEquals("Error de validación\n", exception.getMessage());
    }

    /**
     * Prueba exitosa para verificar la autenticación de un usuario JWT válido.
     */
    @Test
    void testVerificarUsuarioJwt_Success() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setEmail("usuario@test.com");

        when(authentication.getName()).thenReturn("usuario@test.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        service.verificarUsuarioJwt(usuario);  // No debe lanzar excepción
    }

    /**
     * Prueba para verificar la autenticación de un usuario JWT que lanza
     * excepción.
     */
    @Test
    void testVerificarUsuarioJwt_ThrowsException() {
        Usuario usuario = new Usuario();
        usuario.setEmail("otro@test.com");

        when(authentication.getName()).thenReturn("usuario@test.com");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("usuario@test.com")).thenReturn(mock(UserDetails.class));

        Exception exception = assertThrows(Exception.class, () -> service.verificarUsuarioJwt(usuario));
        assertEquals("No tienes permiso para realizar acciones a este usuario.", exception.getMessage());
    }

    /**
     * Prueba para verificar que un usuario es administrador.
     */
    @Test
    void testIsUserAdmin_ReturnsTrue() {
        // Mock de UserDetails
        UserDetails userDetails = mock(UserDetails.class);

        // Crear una lista con una autoridad de administrador, usando el tipo genérico correcto
        Collection authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Simular que getAuthorities retorna la lista de autoridades
        when(userDetails.getAuthorities()).thenReturn(authorities);

        // Simular la carga de UserDetails por nombre de usuario
        when(userDetailsService.loadUserByUsername("admin@test.com")).thenReturn(userDetails);

        // Llamar al método que estamos probando
        boolean isAdmin = service.isUserAdmin("admin@test.com");

        // Verificar que el usuario es administrador
        assertTrue(isAdmin);
    }

    /**
     * Prueba para verificar que un usuario no es administrador.
     */
    @Test
    void testIsUserAdmin_ReturnsFalse() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(userDetailsService.loadUserByUsername("user@test.com")).thenReturn(userDetails);

        boolean isAdmin = service.isUserAdmin("user@test.com");
        assertFalse(isAdmin);
    }
}
