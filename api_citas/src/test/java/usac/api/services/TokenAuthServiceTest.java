package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.Scheduled;
import usac.api.models.TokenAuth;
import usac.api.repositories.TokenAuthRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenAuthServiceTest {

    @InjectMocks
    private TokenAuthService tokenAuthService;

    @Mock
    private TokenAuthRepository tokenAuthRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTokenExitoso() throws Exception {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        when(tokenAuthRepository.existsByEmail(email)).thenReturn(false);
        when(tokenAuthRepository.save(any(TokenAuth.class))).thenAnswer(invocation -> {
            TokenAuth tokenAuth = invocation.getArgument(0);
            tokenAuth.setToken(token);
            tokenAuth.setFechaHoraExpiracion(expirationTime);
            return tokenAuth;
        });

        TokenAuth tokenAuth = tokenAuthService.generateToken(email);

        assertNotNull(tokenAuth);
        assertEquals(email, tokenAuth.getEmail());
        assertEquals(token, tokenAuth.getToken());
        assertEquals(expirationTime, tokenAuth.getFechaHoraExpiracion());
        verify(tokenAuthRepository, times(1)).save(any(TokenAuth.class));
    }

    @Test
    void testGenerateTokenEmailExistente() {
        String email = "test@example.com";
        when(tokenAuthRepository.existsByEmail(email)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> tokenAuthService.generateToken(email));
        assertEquals("El email ya tiene un token registrado.", exception.getMessage());
    }

    @Test
    void testValidateTokenExitoso() throws Exception {
        String token = "sample-token";
        TokenAuth tokenAuth = new TokenAuth("test@example.com", token, LocalDateTime.now().plusMinutes(5));

        when(tokenAuthRepository.findOneByToken(token)).thenReturn(Optional.of(tokenAuth));

        TokenAuth result = tokenAuthService.validateToken(token);

        assertNotNull(result);
        assertEquals(token, result.getToken());
        verify(tokenAuthRepository, never()).delete(any(TokenAuth.class));
    }

    @Test
    void testValidateTokenExpirado() {
        String token = "expired-token";
        TokenAuth expiredToken = new TokenAuth("test@example.com", token, LocalDateTime.now().minusMinutes(1));

        when(tokenAuthRepository.findOneByToken(token)).thenReturn(Optional.of(expiredToken));

        Exception exception = assertThrows(Exception.class, () -> tokenAuthService.validateToken(token));
        assertEquals("El token ha expirado.", exception.getMessage());
        verify(tokenAuthRepository, times(1)).delete(expiredToken);
    }

    @Test
    void testValidateTokenNoEncontrado() {
        String token = "non-existent-token";

        when(tokenAuthRepository.findOneByToken(token)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> tokenAuthService.validateToken(token));
        assertEquals("Token no encontrado.", exception.getMessage());
    }

    @Test
    void testEliminarTokenExitoso() {
        String token = "test-token";
        TokenAuth tokenAuth = new TokenAuth("test@example.com", token, LocalDateTime.now().plusMinutes(5));

        when(tokenAuthRepository.deleteByToken(token)).thenReturn(1L);

        assertTrue(tokenAuthService.eliminarToken(tokenAuth));
        verify(tokenAuthRepository, times(1)).deleteByToken(token);
    }

    @Test
    void testEliminarTokenFallido() {
        String token = "test-token";
        TokenAuth tokenAuth = new TokenAuth("test@example.com", token, LocalDateTime.now().plusMinutes(5));

        when(tokenAuthRepository.deleteByToken(token)).thenReturn(0L);

        assertFalse(tokenAuthService.eliminarToken(tokenAuth));
        verify(tokenAuthRepository, times(1)).deleteByToken(token);
    }

}
