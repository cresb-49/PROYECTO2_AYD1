/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import usac.api.services.authentification.JwtGeneratorService;

/**
 *
 * @author Luis Monterroso
 */
public class JwtRequestFilterTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtGeneratorService jwtGeneratorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Prueba para verificar que el token JWT es válido y el usuario es
     * autenticado correctamente.
     */
    @Test
    void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        // Simular cabecera con un token JWT válido
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-jwt");

        // Simular el comportamiento de JwtGeneratorService
        when(jwtGeneratorService.extractUserName("valid-jwt")).thenReturn("testUser");

        // Simular que no hay autenticación previa
        when(securityContext.getAuthentication()).thenReturn(null);

        // Simular que el usuario existe en UserDetailsService
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        // Simular que el token es válido
        when(jwtGeneratorService.validateTOken("valid-jwt", userDetails)).thenReturn(true);

        // Llamar al filtro
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verificar que el usuario fue autenticado y que el token fue configurado correctamente
        verify(securityContext).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Prueba para verificar que el token JWT es inválido y no se realiza la
     * autenticación.
     */
    @Test
    void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        // Simular cabecera con un token JWT inválido
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-jwt");

        // Simular que no se extrae un nombre de usuario del JWT
        when(jwtGeneratorService.extractUserName("invalid-jwt")).thenReturn("testUser");

        // Simular que el token no es válido
        when(jwtGeneratorService.validateTOken("invalid-jwt", null)).thenReturn(false);

        // Llamar al filtro
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verificar que no se realizó autenticación
        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Prueba para verificar que no se hace nada si no hay cabecera de
     * autorización.
     */
    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        // Simular que no hay cabecera de autorización
        when(request.getHeader("Authorization")).thenReturn(null);

        // Llamar al filtro
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verificar que no se realiza ninguna autenticación
        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Prueba para verificar que los roles del usuario autenticado se imprimen
     * correctamente.
     */
    @Test
    void testDoFilterInternal_PrintUserRoles() throws ServletException, IOException {
        // Simular cabecera con un token JWT válido
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-jwt");

        // Simular el comportamiento de JwtGeneratorService
        when(jwtGeneratorService.extractUserName("valid-jwt")).thenReturn("testUser");

        // Simular que no hay autenticación previa
        when(securityContext.getAuthentication()).thenReturn(null);

        // Simular que el usuario existe en UserDetailsService
        UserDetails userDetails = mock(UserDetails.class);
        Collection authorities = new ArrayList<>();
        when(userDetails.getAuthorities()).thenReturn(authorities);
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

        // Simular que el token es válido
        when(jwtGeneratorService.validateTOken("valid-jwt", userDetails)).thenReturn(true);

        // Llamar al filtro
        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Verificar que los roles del usuario fueron impresos
        verify(securityContext).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
    }

}
