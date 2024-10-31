package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTokenAuthRequestTest {

    @InjectMocks
    private CreateTokenAuthRequest createTokenAuthRequest;

    @Mock
    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba para verificar que el método setEmail asigna el valor
     * correctamente.
     */
    @Test
    void testSetEmail() {
        String testEmail = "test@example.com";
        createTokenAuthRequest.setEmail(testEmail);
        assertEquals(testEmail, createTokenAuthRequest.getEmail());
    }

    /**
     * Prueba para verificar que la validación falla cuando el email es nulo.
     */
    @Test
    void testEmailNoPuedeSerNulo() {
        createTokenAuthRequest.setEmail(null);
        Set<ConstraintViolation<CreateTokenAuthRequest>> violations = validator.validate(createTokenAuthRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El email del cliente no puede ser nulo")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el email está vacío.
     */
    @Test
    void testEmailNoPuedeEstarVacio() {
        createTokenAuthRequest.setEmail("");
        Set<ConstraintViolation<CreateTokenAuthRequest>> violations = validator.validate(createTokenAuthRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El email del cliente no puede estar vacío.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el email no es
     * válido.
     */
    @Test
    void testEmailDebeSerValido() {
        createTokenAuthRequest.setEmail("invalid-email");
        Set<ConstraintViolation<CreateTokenAuthRequest>> violations = validator.validate(createTokenAuthRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El email debe ser un email valido.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el email excede los
     * 250 caracteres.
     */
    @Test
    void testEmailLongitudMaxima() {
        String longEmail = "a".repeat(251) + "@example.com";
        createTokenAuthRequest.setEmail(longEmail);
        Set<ConstraintViolation<CreateTokenAuthRequest>> violations = validator.validate(createTokenAuthRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El email del cliente debe tener entre 1 y 250 caracteres.")));
    }

    /**
     * Prueba para verificar que el email pasa todas las validaciones con un
     * valor válido.
     */
    @Test
    void testEmailValido() {
        String validEmail = "user@example.com";
        createTokenAuthRequest.setEmail(validEmail);
        Set<ConstraintViolation<CreateTokenAuthRequest>> violations = validator.validate(createTokenAuthRequest);
        assertTrue(violations.isEmpty());
        assertEquals(validEmail, createTokenAuthRequest.getEmail());
    }
}
