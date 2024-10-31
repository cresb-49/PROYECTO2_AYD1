package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReservacionRequestTest {

    @InjectMocks
    private ReservacionRequest reservacionRequest;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        reservacionRequest = new ReservacionRequest(
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                LocalDate.of(2024, 10, 10)
        );
    }

    /**
     * Prueba para verificar que el método setHoraInicio asigna el valor
     * correctamente.
     */
    @Test
    void testSetHoraInicio() {
        reservacionRequest.setHoraInicio(LocalTime.of(10, 0));
        assertEquals(LocalTime.of(10, 0), reservacionRequest.getHoraInicio());
    }

    /**
     * Prueba para verificar que el método setHoraFin asigna el valor
     * correctamente.
     */
    @Test
    void testSetHoraFin() {
        reservacionRequest.setHoraFin(LocalTime.of(12, 0));
        assertEquals(LocalTime.of(12, 0), reservacionRequest.getHoraFin());
    }

    /**
     * Prueba para verificar que el método setFechaReservacion asigna el valor
     * correctamente.
     */
    @Test
    void testSetFechaReservacion() {
        LocalDate nuevaFecha = LocalDate.of(2024, 12, 1);
        reservacionRequest.setFechaReservacion(nuevaFecha);
        assertEquals(nuevaFecha, reservacionRequest.getFechaReservacion());
    }

    /**
     * Prueba para verificar que la validación falla cuando horaInicio es nula.
     */
    @Test
    void testHoraInicioNoPuedeSerNula() {
        reservacionRequest.setHoraInicio(null);
        Set<ConstraintViolation<ReservacionRequest>> violations = validator.validate(reservacionRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La hora de inicio de la reservacion.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando fechaReservacion es
     * nula.
     */
    @Test
    void testFechaReservacionNoPuedeSerNula() {
        reservacionRequest.setFechaReservacion(null);
        Set<ConstraintViolation<ReservacionRequest>> violations = validator.validate(reservacionRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La fecha de la reservacion no puede ser nula")));
    }

    /**
     * Prueba para verificar que una instancia válida pasa todas las
     * restricciones.
     */
    @Test
    void testValoresValidos() {
        Set<ConstraintViolation<ReservacionRequest>> violations = validator.validate(reservacionRequest);
        assertTrue(violations.isEmpty());
    }
}
