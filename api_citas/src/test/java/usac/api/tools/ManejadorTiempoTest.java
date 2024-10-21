/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.tools;

import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author luid
 */
public class ManejadorTiempoTest {

    @InjectMocks
    private ManejadorTiempo manejadorFechas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para verificar que el método localDateANombreDia devuelve el
     * nombre del día correctamente en español.
     */
    @Test
    void testLocalDateANombreDia_Lunes() {
        // Establecer una fecha conocida
        LocalDate fechaLunes = LocalDate.of(2024, 10, 7); // Este es un lunes

        // Ejecutar el método a probar
        String nombreDia = manejadorFechas.localDateANombreDia(fechaLunes);

        // Verificar que el resultado sea "lunes"
        assertEquals("lunes", nombreDia);
    }

    @Test
    void testLocalDateANombreDia_Miercoles() {
        // Establecer una fecha conocida
        LocalDate fechaMiercoles = LocalDate.of(2024, 10, 9); // Este es un miércoles

        // Ejecutar el método a probar
        String nombreDia = manejadorFechas.localDateANombreDia(fechaMiercoles);

        // Verificar que el resultado sea "miércoles"
        assertEquals("miércoles", nombreDia);
    }

    @Test
    void testLocalDateANombreDia_Domingo() {
        // Establecer una fecha conocida
        LocalDate fechaDomingo = LocalDate.of(2024, 10, 13); // Este es un domingo

        // Ejecutar el método a probar
        String nombreDia = manejadorFechas.localDateANombreDia(fechaDomingo);

        // Verificar que el resultado sea "domingo"
        assertEquals("domingo", nombreDia);
    }

    /**
     * Prueba que verifica que un horario está dentro de los límites (apertura y
     * cierre).
     */
    @Test
    void testHorarioDentroDeLimites() {
        // Definir horarios de apertura y cierre
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(18, 0);

        // Definir un horario de comparación que está dentro del rango
        LocalTime inicioComparacion = LocalTime.of(9, 0);
        LocalTime finComparacion = LocalTime.of(17, 0);

        // Verificar que el horario esté dentro de los límites
        boolean resultado = manejadorFechas.isHorarioEnLimites(apertura, cierre, inicioComparacion, finComparacion);
        assertTrue(resultado);
    }

    /**
     * Prueba que verifica que un horario no está dentro de los límites.
     */
    @Test
    void testHorarioFueraDeLimites_InicioAntes() {
        // Definir horarios de apertura y cierre
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(18, 0);

        // Definir un horario de comparación que empieza antes de la apertura
        LocalTime inicioComparacion = LocalTime.of(7, 0);
        LocalTime finComparacion = LocalTime.of(17, 0);

        // Verificar que el horario no esté dentro de los límites
        boolean resultado = manejadorFechas.isHorarioEnLimites(apertura, cierre, inicioComparacion, finComparacion);
        assertFalse(resultado);
    }

    /**
     * Prueba que verifica que un horario no está dentro de los límites cuando
     * la hora de fin es después del cierre.
     */
    @Test
    void testHorarioFueraDeLimites_FinDespues() {
        // Definir horarios de apertura y cierre
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(18, 0);

        // Definir un horario de comparación que termina después del cierre
        LocalTime inicioComparacion = LocalTime.of(9, 0);
        LocalTime finComparacion = LocalTime.of(19, 0);

        // Verificar que el horario no esté dentro de los límites
        boolean resultado = manejadorFechas.isHorarioEnLimites(apertura, cierre, inicioComparacion, finComparacion);
        assertFalse(resultado);
    }

    /**
     * Prueba que verifica que un horario que empieza y termina exactamente en
     * los límites es válido.
     */
    @Test
    void testHorarioExactamenteEnLosLimites() {
        // Definir horarios de apertura y cierre
        LocalTime apertura = LocalTime.of(8, 0);
        LocalTime cierre = LocalTime.of(18, 0);

        // Definir un horario de comparación que empieza y termina exactamente en los límites
        LocalTime inicioComparacion = LocalTime.of(8, 0);
        LocalTime finComparacion = LocalTime.of(18, 0);

        // Verificar que el horario esté dentro de los límites
        boolean resultado = manejadorFechas.isHorarioEnLimites(apertura, cierre, inicioComparacion, finComparacion);
        assertTrue(resultado);
    }
}
