package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaServicioTest {

    private Reserva reserva;
    private Empleado empleado;
    private Servicio servicio;
    private ReservaServicio reservaServicio;

    private static Validator validator;

    @BeforeEach
    void setUp() {
        // Configuración del validador para probar las restricciones de validación
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        reserva = new Reserva();
        empleado = new Empleado();
        servicio = new Servicio();
        reservaServicio = new ReservaServicio(reserva, empleado, servicio);
    }

    @Test
    void testConstructor() {
        assertNotNull(reservaServicio);
        assertEquals(reserva, reservaServicio.getReserva());
        assertEquals(empleado, reservaServicio.getEmpleado());
        assertEquals(servicio, reservaServicio.getServicio());
    }

    @Test
    void testSettersAndGetters() {
        Reserva nuevaReserva = new Reserva();
        Empleado nuevoEmpleado = new Empleado();
        Servicio nuevoServicio = new Servicio();

        reservaServicio.setReserva(nuevaReserva);
        reservaServicio.setEmpleado(nuevoEmpleado);
        reservaServicio.setServicio(nuevoServicio);

        assertEquals(nuevaReserva, reservaServicio.getReserva());
        assertEquals(nuevoEmpleado, reservaServicio.getEmpleado());
        assertEquals(nuevoServicio, reservaServicio.getServicio());
    }

    @Test
    void testReservaNotNullConstraint() {
        reservaServicio.setReserva(null);

        Set<ConstraintViolation<ReservaServicio>> violations = validator.validate(reservaServicio);
        assertFalse(violations.isEmpty());

        ConstraintViolation<ReservaServicio> violation = violations.stream()
                .filter(v -> "La reserva no puede ser nula.".equals(v.getMessage()))
                .findFirst()
                .orElse(null);

        assertNotNull(violation, "Debería existir una violación de la restricción 'La reserva no puede ser nula.'");
    }

    @Test
    void testEmpleadoNotNullConstraint() {
        reservaServicio.setEmpleado(null);

        Set<ConstraintViolation<ReservaServicio>> violations = validator.validate(reservaServicio);
        assertFalse(violations.isEmpty());

        ConstraintViolation<ReservaServicio> violation = violations.stream()
                .filter(v -> "El empleado no puede ser nulo.".equals(v.getMessage()))
                .findFirst()
                .orElse(null);

        assertNotNull(violation, "Debería existir una violación de la restricción 'El empleado no puede ser nulo.'");
    }

    @Test
    void testServicioNotNullConstraint() {
        reservaServicio.setServicio(null);

        Set<ConstraintViolation<ReservaServicio>> violations = validator.validate(reservaServicio);
        assertFalse(violations.isEmpty());

        ConstraintViolation<ReservaServicio> violation = violations.stream()
                .filter(v -> "El servicio no puede ser nulo.".equals(v.getMessage()))
                .findFirst()
                .orElse(null);

        assertNotNull(violation, "Debería existir una violación de la restricción 'El servicio no puede ser nulo.'");
    }
}
