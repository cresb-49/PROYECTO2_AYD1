package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreacionClienteRequestTest {

    private CreacionClienteRequest creacionClienteRequest;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        creacionClienteRequest = new CreacionClienteRequest(
                "validToken",
                "123456",
                "1234567890123",
                "12345678",
                "Nombre",
                "Apellido",
                "Password123"
        );
    }

    /**
     * Prueba para verificar que el método setTokenAuth asigna el valor
     * correctamente.
     */
    @Test
    void testSetTokenAuth() {
        creacionClienteRequest.setTokenAuth("newToken");
        assertEquals("newToken", creacionClienteRequest.getTokenAuth());
    }

    /**
     * Prueba para verificar que el método setNit asigna el valor correctamente.
     */
    @Test
    void testSetNit() {
        creacionClienteRequest.setNit("654321");
        assertEquals("654321", creacionClienteRequest.getNit());
    }

    /**
     * Prueba para verificar que el método setCui asigna el valor correctamente.
     */
    @Test
    void testSetCui() {
        creacionClienteRequest.setCui("9876543210123");
        assertEquals("9876543210123", creacionClienteRequest.getCui());
    }

    /**
     * Prueba para verificar que el método setPhone asigna el valor
     * correctamente.
     */
    @Test
    void testSetPhone() {
        creacionClienteRequest.setPhone("87654321");
        assertEquals("87654321", creacionClienteRequest.getPhone());
    }

    /**
     * Prueba para verificar que el método setNombres asigna el valor
     * correctamente.
     */
    @Test
    void testSetNombres() {
        creacionClienteRequest.setNombres("NewName");
        assertEquals("NewName", creacionClienteRequest.getNombres());
    }

    /**
     * Prueba para verificar que el método setApellidos asigna el valor
     * correctamente.
     */
    @Test
    void testSetApellidos() {
        creacionClienteRequest.setApellidos("NewSurname");
        assertEquals("NewSurname", creacionClienteRequest.getApellidos());
    }

    /**
     * Prueba para verificar que el método setPassword asigna el valor
     * correctamente.
     */
    @Test
    void testSetPassword() {
        creacionClienteRequest.setPassword("NewPassword123");
        assertEquals("NewPassword123", creacionClienteRequest.getPassword());
    }

    /**
     * Prueba para verificar que la validación falla cuando el tokenAuth está
     * vacío.
     */
    @Test
    void testTokenAuthNoPuedeEstarVacio() {
        creacionClienteRequest.setTokenAuth("");
        Set<ConstraintViolation<CreacionClienteRequest>> violations = validator.validate(creacionClienteRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El token de verificacion no puede estar vacio.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el nit es nulo.
     */
    @Test
    void testNitNoPuedeSerNulo() {
        creacionClienteRequest.setNit(null);
        Set<ConstraintViolation<CreacionClienteRequest>> violations = validator.validate(creacionClienteRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nit del cliente no puede ser nulo")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el nit no cumple con
     * el tamaño.
     */
    @Test
    void testNitTamanoInvalido() {
        creacionClienteRequest.setNit("123");
        Set<ConstraintViolation<CreacionClienteRequest>> violations = validator.validate(creacionClienteRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nit del cliente debe tener entre 6 y 12 caracteres.")));
    }

    /**
     * Prueba para verificar que los valores válidos pasan las validaciones.
     */
    @Test
    void testValoresValidos() {
        Set<ConstraintViolation<CreacionClienteRequest>> violations = validator.validate(creacionClienteRequest);
        assertTrue(violations.isEmpty());
    }

    // Puedes seguir con pruebas similares para "cui", "phone", "nombres", "apellidos" y "password"
}
