/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services.authentification;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Luis Monterroso
 */
public class JwtGeneratorServiceTest {

    private JwtGeneratorService jwtGeneratorService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtGeneratorService = new JwtGeneratorService();
    }

    /**
     * Prueba para verificar que el nombre de usuario se extrae correctamente
     * del token.
     */
    @Test
    void testExtractUserName() {
        String token = generateTestToken("testUser");
        String username = jwtGeneratorService.extractUserName(token);
        assertEquals("testUser", username);
    }

    /**
     * Prueba para verificar que la fecha de expiración se extrae correctamente
     * del token.
     */
    @Test
    void testExtractExpiration() {
        String token = generateTestToken("testUser");
        Date expiration = jwtGeneratorService.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    /**
     * Prueba para verificar que se genera correctamente el token para un
     * usuario.
     */
    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        Collection authorities = new ArrayList<>();
        when(userDetails.getAuthorities()).thenReturn(authorities);

        String token = jwtGeneratorService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    /**
     * Prueba para verificar que el token se valida correctamente para un
     * usuario.
     */
    @Test
    void testValidateToken_Success() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = generateTestToken("testUser");

        boolean isValid = jwtGeneratorService.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    /**
     * Prueba para verificar que el token expirado es detectado correctamente.
     */
    @Test
    void testValidateToken_TokenExpired() {
        String expiredToken = generateExpiredToken("testUser");
        when(userDetails.getUsername()).thenReturn("testUser");

        // Capturamos la excepción para que no detenga el test
        try {
            boolean isValid = jwtGeneratorService.validateToken(expiredToken, userDetails);
            // Como el token está expirado, esperamos que isValid sea false
            assertFalse(isValid);
        } catch (Exception e) {
            // Si el token está expirado, la excepción es lanzada correctamente
            assertTrue(e.getMessage().contains("JWT expired"));
        }
    }

    /**
     * Método auxiliar para generar un token de prueba válido.
     */
    private String generateTestToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", new ArrayList<>());
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, "llave_secreta").compact();
    }

    /**
     * Método auxiliar para generar un token expirado de prueba.
     */
    private String generateExpiredToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", new ArrayList<>());
        return Jwts.builder().setClaims(claims).setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 10)) // 10 horas atrás
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // Expirado hace 1 hora
                .signWith(SignatureAlgorithm.HS256, "llave_secreta").compact();
    }
}
