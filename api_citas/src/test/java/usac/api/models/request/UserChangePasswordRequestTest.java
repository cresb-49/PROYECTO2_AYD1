package usac.api.models.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para UserChangePasswordRequest. Se validan los
 * atributos y sus restricciones.
 */
public class UserChangePasswordRequestTest {

    private Validator validator;

    /**
     * Inicializar el validador antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba que valida un objeto correcto de UserChangePasswordRequest.
     */
    @Test
    void testValidUserChangePasswordRequest() {
        UserChangePasswordRequest request = new UserChangePasswordRequest(1L, "user@example.com", "oldPassword", "newPassword");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "No deben haber violaciones de validación.");
    }

    /**
     * Prueba para validar que se genera un error cuando el ID está vacío.
     */
    @Test
    void testInvalidId() {
        UserChangePasswordRequest request = new UserChangePasswordRequest(null,
                "user@example.com", "oldPassword", "newPassword");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(
                request);

        assertEquals(1, violations.size(), "Debe haber 1 violación de validación.");
        assertEquals("El id no puede ser nulo.", violations.iterator().next().getMessage());
    }

    /**
     * Prueba para validar que se genera un error cuando el email está vacío.
     */
    @Test
    void testInvalidEmail() {
        UserChangePasswordRequest request = new UserChangePasswordRequest(1L, "", "oldPassword", "newPassword");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size(), "Debe haber 1 violación de validación.");
        assertEquals("La email no puede estar vacía.", violations.iterator().next().getMessage());
    }

    /**
     * Prueba para validar que se genera un error cuando la contraseña actual
     * está vacía.
     */
    @Test
    void testInvalidPassword() {
        UserChangePasswordRequest request = new UserChangePasswordRequest(1L,
                "user@example.com", "", "newPassword");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(
                request);

        assertEquals(1, violations.size(), "Debe haber 1 violación de validación.");
        assertEquals("La contraseña actual no puede estar vacía.",
                violations.iterator().next().getMessage());
    }

    /**
     * Prueba para validar que se genera un error cuando la nueva contraseña
     * está vacía.
     */
    @Test
    void testInvalidNewPassword() {
        UserChangePasswordRequest request = new UserChangePasswordRequest(1L,
                "user@example.com", "oldPassword", "");

        Set<ConstraintViolation<UserChangePasswordRequest>> violations = validator.validate(
                request);

        assertEquals(2, violations.size(), "Debe haber 2 violación de validación.");
    }

    /**
     * Prueba para validar los setters y getters.
     */
    @Test
    void testSettersAndGetters() {
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setId(1L);
        request.setEmail("user@example.com");
        request.setPassword("oldPassword");
        request.setNewPassword("newPassword");

        assertEquals(1L, request.getId());
        assertEquals("user@example.com", request.getEmail());
        assertEquals("oldPassword", request.getPassword());
        assertEquals("newPassword", request.getNewPassword());
    }
}
