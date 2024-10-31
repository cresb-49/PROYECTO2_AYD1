package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FacturaTest {

    private Factura factura;
    private Validator validator;

    @BeforeEach
    void setUp() {
        factura = new Factura("Juan Perez", "123456789", "Consulta médica", "Examen y diagnóstico", 150.0);

        // Configuración de validación
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testConstructor() {
        assertNotNull(factura);
        assertEquals("Juan Perez", factura.getNombre());
        assertEquals("123456789", factura.getNit());
        assertEquals("Consulta médica", factura.getConcepto());
        assertEquals("Examen y diagnóstico", factura.getDetalle());
        assertEquals(150.0, factura.getTotal());
    }

    @Test
    void testSettersAndGetters() {
        factura.setNombre("Carlos Garcia");
        factura.setNit("987654321");
        factura.setConcepto("Consulta dental");
        factura.setDetalle("Evaluación dental completa");
        factura.setTotal(200.0);

        assertEquals("Carlos Garcia", factura.getNombre());
        assertEquals("987654321", factura.getNit());
        assertEquals("Consulta dental", factura.getConcepto());
        assertEquals("Evaluación dental completa", factura.getDetalle());
        assertEquals(200.0, factura.getTotal());
    }

    @Test
    void testValidacionNombreNotBlank() {
        factura.setNombre("");
        Set<ConstraintViolation<Factura>> violations = validator.validate(factura);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidacionNitSize() {
        factura.setNit("123");  // Nit menor de 6 caracteres
        Set<ConstraintViolation<Factura>> violations = validator.validate(factura);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testValidacionTotalMinValue() {
        factura.setTotal(0.5);  // Menor al valor mínimo de 1
        Set<ConstraintViolation<Factura>> violations = validator.validate(factura);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testReservaAssociation() {
        Reserva reserva = new Reserva();
        factura.setReserva(reserva);
        assertEquals(reserva, factura.getReserva());
    }
}
