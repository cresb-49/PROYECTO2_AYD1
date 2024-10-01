/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luis Monterroso
 */
public class PasswordChangeRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        // Inicializar el validador de Hibernate Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba para verificar que el constructor con parámetros funciona
     * correctamente.
     */
    @Test
    void testConstructorWithParameters() {
        PasswordChangeRequest request = new PasswordChangeRequest("newPassword123", "recoveryCode");
        assertEquals("newPassword123", request.getNuevaPassword());
        assertEquals("recoveryCode", request.getCodigo());
    }

    /**
     * Prueba para verificar que el constructor sin parámetros funciona
     * correctamente.
     */
    @Test
    void testNoArgsConstructor() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        assertNull(request.getNuevaPassword());
        assertNull(request.getCodigo());
    }

    /**
     * Prueba para verificar los métodos setter y getter de nuevaPassword.
     */
    @Test
    void testSetGetNuevaPassword() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setNuevaPassword("testPassword");
        assertEquals("testPassword", request.getNuevaPassword());
    }

    /**
     * Prueba para verificar los métodos setter y getter de codigo.
     */
    @Test
    void testSetGetCodigo() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCodigo("testCode");
        assertEquals("testCode", request.getCodigo());
    }

    /**
     * Prueba que verifica que la validación falla si la nueva contraseña es
     * nula.
     */
    @Test
    void testNuevaPasswordNotNullValidation() {
        PasswordChangeRequest request = new PasswordChangeRequest(null, "validCode");

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La contraseña no puede ser nula.")));
    }

    /**
     * Prueba que verifica que la validación falla si el código de recuperación
     * es nulo.
     */
    @Test
    void testCodigoNotNullValidation() {
        PasswordChangeRequest request = new PasswordChangeRequest("validPassword", null);

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El codigo de recuperación no puede ser nula.")));
    }

    /**
     * Prueba que verifica que la validación falla si la nueva contraseña está
     * en blanco.
     */
    @Test
    void testNuevaPasswordNotBlankValidation() {
        PasswordChangeRequest request = new PasswordChangeRequest("", "validCode");

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La contraseña no puede estar vacía.")));
    }

    /**
     * Prueba que verifica que la validación falla si el código de recuperación
     * está en blanco.
     */
    @Test
    void testCodigoNotBlankValidation() {
        PasswordChangeRequest request = new PasswordChangeRequest("validPassword", "");

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El codigo de recuperación no puede estar vacío.")));
    }

    /**
     * Prueba que verifica que la validación falla si la contraseña excede el
     * tamaño máximo de caracteres.
     */
    @Test
    void testNuevaPasswordSizeValidation() {
        String longPassword = "a".repeat(251);  // Contraseña con más de 250 caracteres
        PasswordChangeRequest request = new PasswordChangeRequest(longPassword, "validCode");

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La contraseña debe tener entre 1 y 250 caracteres.")));
    }

    /**
     * Prueba exitosa que valida una contraseña y código correctos.
     */
    @Test
    void testValidPasswordChangeRequest() {
        PasswordChangeRequest request = new PasswordChangeRequest("ValidPassword123", "validCode");

        Set<ConstraintViolation<PasswordChangeRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());  // No debería haber violaciones
    }
}
