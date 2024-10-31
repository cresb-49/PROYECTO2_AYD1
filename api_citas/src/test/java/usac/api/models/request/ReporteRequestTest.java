package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReporteRequestTest {

    @InjectMocks
    private ReporteRequest reporteRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inicializar con fechas específicas
        reporteRequest = new ReporteRequest(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    }

    /**
     * Prueba para verificar que el método getFecha1 retorna el valor correcto.
     */
    @Test
    void testGetFecha1() {
        assertEquals(LocalDate.of(2024, 1, 1), reporteRequest.getFecha1());
    }

    /**
     * Prueba para verificar que el método setFecha1 asigna el valor
     * correctamente.
     */
    @Test
    void testSetFecha1() {
        LocalDate nuevaFecha1 = LocalDate.of(2024, 6, 15);
        reporteRequest.setFecha1(nuevaFecha1);
        assertEquals(nuevaFecha1, reporteRequest.getFecha1());
    }

    /**
     * Prueba para verificar que el método getFecha2 retorna el valor correcto.
     */
    @Test
    void testGetFecha2() {
        assertEquals(LocalDate.of(2024, 12, 31), reporteRequest.getFecha2());
    }

    /**
     * Prueba para verificar que el método setFecha2 asigna el valor
     * correctamente.
     */
    @Test
    void testSetFecha2() {
        LocalDate nuevaFecha2 = LocalDate.of(2024, 11, 15);
        reporteRequest.setFecha2(nuevaFecha2);
        assertEquals(nuevaFecha2, reporteRequest.getFecha2());
    }

    /**
     * Prueba para verificar la creación del constructor sin parámetros.
     */
    @Test
    void testConstructorSinParametros() {
        ReporteRequest reporteVacio = new ReporteRequest();
        assertNull(reporteVacio.getFecha1());
        assertNull(reporteVacio.getFecha2());
    }

    /**
     * Prueba para verificar la creación del constructor con parámetros.
     */
    @Test
    void testConstructorConParametros() {
        LocalDate fecha1 = LocalDate.of(2023, 5, 10);
        LocalDate fecha2 = LocalDate.of(2023, 6, 15);
        ReporteRequest reporteConFechas = new ReporteRequest(fecha1, fecha2);

        assertEquals(fecha1, reporteConFechas.getFecha1());
        assertEquals(fecha2, reporteConFechas.getFecha2());
    }
}
