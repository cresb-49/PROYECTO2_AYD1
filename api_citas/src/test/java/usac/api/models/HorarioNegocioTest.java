package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HorarioNegocioTest {

    private Dia dia;
    private Negocio negocio;
    private LocalTime apertura;
    private LocalTime cierre;
    private HorarioNegocio horarioNegocio;

    private static Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        dia = new Dia();
        dia.setNombre("Lunes");
        negocio = new Negocio();
        apertura = LocalTime.of(9, 0);
        cierre = LocalTime.of(18, 0);
        horarioNegocio = new HorarioNegocio(dia, negocio, apertura, cierre);
    }

    @Test
    void testConstructor() {
        assertNotNull(horarioNegocio);
        assertEquals(dia, horarioNegocio.getDia());
        assertEquals(negocio, horarioNegocio.getNegocio());
        assertEquals(apertura, horarioNegocio.getApertura());
        assertEquals(cierre, horarioNegocio.getCierre());
    }

    @Test
    void testSettersAndGetters() {
        Dia nuevoDia = new Dia();
        nuevoDia.setNombre("Martes");
        Negocio nuevoNegocio = new Negocio();
        LocalTime nuevaApertura = LocalTime.of(8, 0);
        LocalTime nuevoCierre = LocalTime.of(17, 0);

        horarioNegocio.setDia(nuevoDia);
        horarioNegocio.setNegocio(nuevoNegocio);
        horarioNegocio.setApertura(nuevaApertura);
        horarioNegocio.setCierre(nuevoCierre);

        assertEquals(nuevoDia, horarioNegocio.getDia());
        assertEquals(nuevoNegocio, horarioNegocio.getNegocio());
        assertEquals(nuevaApertura, horarioNegocio.getApertura());
        assertEquals(nuevoCierre, horarioNegocio.getCierre());
    }

    @Test
    void testAperturaNotNullConstraint() {
        horarioNegocio.setApertura(null);

        Set<ConstraintViolation<HorarioNegocio>> violations = validator.validate(horarioNegocio);
        assertFalse(violations.isEmpty());

        ConstraintViolation<HorarioNegocio> violation = violations.stream()
                .filter(v -> "La hora de apertura no puede ser nula".equals(v.getMessage()))
                .findFirst()
                .orElse(null);

        assertNotNull(violation, "Debería existir una violación de la restricción 'La hora de apertura no puede ser nula'");
    }

    @Test
    void testCierreNotNullConstraint() {
        horarioNegocio.setCierre(null);

        Set<ConstraintViolation<HorarioNegocio>> violations = validator.validate(horarioNegocio);
        assertFalse(violations.isEmpty());

        ConstraintViolation<HorarioNegocio> violation = violations.stream()
                .filter(v -> "La hora de cierre no puede ser nula".equals(v.getMessage()))
                .findFirst()
                .orElse(null);

        assertNotNull(violation, "Debería existir una violación de la restricción 'La hora de cierre no puede ser nula'");
    }

    @Test
    void testToString() {
        String expected = "HorarioNegocio [apertura=" + apertura + ", cierre=" + cierre + ", dia=" + dia.getNombre()
                + ", negocio=" + negocio.getId() + ", deletedAt=" + horarioNegocio.getDeletedAt() + "]";
        assertEquals(expected, horarioNegocio.toString());
    }
}
