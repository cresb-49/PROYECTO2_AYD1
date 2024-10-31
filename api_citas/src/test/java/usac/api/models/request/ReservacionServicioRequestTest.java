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

public class ReservacionServicioRequestTest {

    @InjectMocks
    private ReservacionServicioRequest reservacionServicioRequest;

    private Validator validator; // Instancia del validador

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        reservacionServicioRequest = new ReservacionServicioRequest(
                1L, 2L, LocalTime.of(10, 0), LocalTime.of(12, 0), LocalDate.of(2024, 12, 25)
        );
    }

    /**
     * Prueba para verificar que el método setEmpleadoId asigna el valor
     * correctamente.
     */
    @Test
    void testSetEmpleadoId() {
        reservacionServicioRequest.setEmpleadoId(3L);
        assertEquals(3L, reservacionServicioRequest.getEmpleadoId());
    }

    /**
     * Prueba para verificar que el método setServicioId asigna el valor
     * correctamente.
     */
    @Test
    void testSetServicioId() {
        reservacionServicioRequest.setServicioId(4L);
        assertEquals(4L, reservacionServicioRequest.getServicioId());
    }

    /**
     * Prueba para verificar que el método setHoraInicio asigna el valor
     * correctamente.
     */
    @Test
    void testSetHoraInicio() {
        reservacionServicioRequest.setHoraInicio(LocalTime.of(9, 0));
        assertEquals(LocalTime.of(9, 0), reservacionServicioRequest.getHoraInicio());
    }

    /**
     * Prueba para verificar que el método setHoraFin asigna el valor
     * correctamente.
     */
    @Test
    void testSetHoraFin() {
        reservacionServicioRequest.setHoraFin(LocalTime.of(13, 0));
        assertEquals(LocalTime.of(13, 0), reservacionServicioRequest.getHoraFin());
    }

    /**
     * Prueba para verificar que el método setFechaReservacion asigna el valor
     * correctamente.
     */
    @Test
    void testSetFechaReservacion() {
        LocalDate nuevaFecha = LocalDate.of(2025, 1, 1);
        reservacionServicioRequest.setFechaReservacion(nuevaFecha);
        assertEquals(nuevaFecha, reservacionServicioRequest.getFechaReservacion());
    }

    /**
     * Prueba para verificar que la validación falla cuando empleadoId es nulo.
     */
    @Test
    void testEmpleadoIdNoPuedeSerNulo() {
        reservacionServicioRequest.setEmpleadoId(null);
        Set<ConstraintViolation<ReservacionServicioRequest>> violations = validator.validate(reservacionServicioRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El id del empleado no puede ser nulo.")));
    }

    /**
     * Prueba para verificar que la validación falla cuando servicioId es nulo.
     */
    @Test
    void testServicioIdNoPuedeSerNulo() {
        reservacionServicioRequest.setServicioId(null);
        Set<ConstraintViolation<ReservacionServicioRequest>> violations = validator.validate(reservacionServicioRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El id del servicio no puede ser nulo.")));
    }

    /**
     * Prueba para verificar que una instancia válida pasa todas las
     * restricciones.
     */
    @Test
    void testValoresValidos() {
        Set<ConstraintViolation<ReservacionServicioRequest>> violations = validator.validate(reservacionServicioRequest);
        assertTrue(violations.isEmpty());
    }

    /**
     * Prueba para verificar que el método getEmpleadoId retorna el valor
     * correcto.
     */
    @Test
    void testGetEmpleadoId() {
        assertEquals(1L, reservacionServicioRequest.getEmpleadoId());
    }

    /**
     * Prueba para verificar que el método getServicioId retorna el valor
     * correcto.
     */
    @Test
    void testGetServicioId() {
        assertEquals(2L, reservacionServicioRequest.getServicioId());
    }

    /**
     * Prueba para verificar que el método getHoraInicio retorna el valor
     * correcto.
     */
    @Test
    void testGetHoraInicio() {
        assertEquals(LocalTime.of(10, 0), reservacionServicioRequest.getHoraInicio());
    }

    /**
     * Prueba para verificar que el método getHoraFin retorna el valor correcto.
     */
    @Test
    void testGetHoraFin() {
        assertEquals(LocalTime.of(12, 0), reservacionServicioRequest.getHoraFin());
    }

    /**
     * Prueba para verificar que el método getFechaReservacion retorna el valor
     * correcto.
     */
    @Test
    void testGetFechaReservacion() {
        assertEquals(LocalDate.of(2024, 12, 25), reservacionServicioRequest.getFechaReservacion());
    }
}
