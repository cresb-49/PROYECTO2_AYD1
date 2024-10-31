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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReporteExportRequestTest {

    @InjectMocks
    private ReporteExportRequest reporteExportRequest;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Inicializar la instancia con valores válidos
        reporteExportRequest = new ReporteExportRequest(
                "reporteVentas", LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31), "pdf"
        );
    }

    /**
     * Prueba para verificar que el método setTipoReporte asigna el valor
     * correctamente.
     */
    @Test
    void testSetTipoReporte() {
        reporteExportRequest.setTipoReporte("reporteClientes");
        assertEquals("reporteClientes", reporteExportRequest.getTipoReporte());
    }

    /**
     * Prueba para verificar que el método setTipoExporte asigna el valor
     * correctamente.
     */
    @Test
    void testSetTipoExporte() {
        reporteExportRequest.setTipoExporte("excel");
        assertEquals("excel", reporteExportRequest.getTipoExporte());
    }

    /**
     * Prueba para verificar que la validación falla cuando tipoReporte no
     * cumple con el patrón permitido.
     */
    @Test
    void testTipoReporteInvalido() {
        reporteExportRequest.setTipoReporte("reporteInvalido");
        Set<ConstraintViolation<ReporteExportRequest>> violations = validator.validate(reporteExportRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("uno de los valores permitidos")));
    }

    /**
     * Prueba para verificar que la validación falla cuando tipoExporte no
     * cumple con el patrón permitido.
     */
    @Test
    void testTipoExporteInvalido() {
        reporteExportRequest.setTipoExporte("txt");
        Set<ConstraintViolation<ReporteExportRequest>> violations = validator.validate(reporteExportRequest);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("uno de los valores permitidos")));
    }

    /**
     * Prueba para verificar que tipoReporte cumple con el valor permitido.
     */
    @Test
    void testTipoReporteValido() {
        reporteExportRequest.setTipoReporte("reporteServicios");
        Set<ConstraintViolation<ReporteExportRequest>> violations = validator.validate(reporteExportRequest);
        assertTrue(violations.isEmpty());
    }

    /**
     * Prueba para verificar que tipoExporte cumple con el valor permitido.
     */
    @Test
    void testTipoExporteValido() {
        reporteExportRequest.setTipoExporte("img");
        Set<ConstraintViolation<ReporteExportRequest>> violations = validator.validate(reporteExportRequest);
        assertTrue(violations.isEmpty());
    }

    /**
     * Prueba para verificar que el método getTipoReporte retorna el valor
     * correcto.
     */
    @Test
    void testGetTipoReporte() {
        assertEquals("reporteVentas", reporteExportRequest.getTipoReporte());
    }

    /**
     * Prueba para verificar que el método getTipoExporte retorna el valor
     * correcto.
     */
    @Test
    void testGetTipoExporte() {
        assertEquals("pdf", reporteExportRequest.getTipoExporte());
    }

    /**
     * Prueba para verificar que la instancia válida pasa todas las
     * restricciones.
     */
    @Test
    void testValoresValidos() {
        Set<ConstraintViolation<ReporteExportRequest>> violations = validator.validate(reporteExportRequest);
        assertTrue(violations.isEmpty());
    }
}
