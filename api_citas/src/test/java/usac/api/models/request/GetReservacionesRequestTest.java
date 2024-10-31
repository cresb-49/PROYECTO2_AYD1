package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GetReservacionesRequestTest {

    @InjectMocks
    private GetReservacionesRequest getReservacionesRequest;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba para verificar que el método setMes asigna el valor correctamente.
     */
    @Test
    void testSetMes() {
        getReservacionesRequest.setMes(5);
        assertEquals(5, getReservacionesRequest.getMes());
    }

    /**
     * Prueba para verificar que el método setAnio asigna el valor
     * correctamente.
     */
    @Test
    void testSetAnio() {
        getReservacionesRequest.setAnio(2023);
        assertEquals(2023, getReservacionesRequest.getAnio());
    }

    /**
     * Prueba para verificar que la validación falla cuando el mes es nulo.
     */
    @Test
    void testMesNoPuedeSerNulo() {
        getReservacionesRequest.setMes(null);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El mes no puede ser nulo.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el mes es menor que
     * 1.
     */
    @Test
    void testMesDebeSerMayorOIgualA1() {
        getReservacionesRequest.setMes(0);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El mes debe ser mayor o igual a 1.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el mes es mayor que
     * 12.
     */
    @Test
    void testMesDebeSerMenorOIgualA12() {
        getReservacionesRequest.setMes(13);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El mes debe ser menor o igual a 12.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el año es nulo.
     */
    @Test
    void testAnioNoPuedeSerNulo() {
        getReservacionesRequest.setAnio(null);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El año no puede ser nulo.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando el año es menor que
     * 1900.
     */
    @Test
    void testAnioDebeSerMayorOIgualA1900() {
        getReservacionesRequest.setAnio(1899);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El año debe ser mayor o igual a 1900.")));
    }

    /**
     * Prueba para verificar que los valores válidos pasan las validaciones.
     */
    @Test
    void testValoresValidos() {
        getReservacionesRequest.setMes(6);
        getReservacionesRequest.setAnio(2022);
        Set<ConstraintViolation<GetReservacionesRequest>> violations = validator.validate(getReservacionesRequest);
        assertTrue(violations.isEmpty());
        assertEquals(6, getReservacionesRequest.getMes());
        assertEquals(2022, getReservacionesRequest.getAnio());
    }
}
