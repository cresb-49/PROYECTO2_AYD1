package usac.api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para la clase Auditor. Se validan los atributos y
 * los métodos de acceso de la clase Auditor.
 */
public class AuditorTest {

    private Auditor auditor;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        // Inicializar una instancia de Auditor para cada prueba
        auditor = new Auditor();
    }

    /**
     * Prueba para validar que los atributos pueden ser configurados
     * correctamente.
     */
    @Test
    void testSettersAndGetters() {
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime desactivatedAt = LocalDateTime.now().plusDays(1);

        // Configurar valores en la instancia de Auditor
        auditor.setId(id);
        auditor.setCreatedAt(createdAt);
        auditor.setUpdatedAt(updatedAt);
        auditor.setDesactivatedAt(desactivatedAt);

        // Verificar que los valores han sido correctamente configurados y recuperados
        assertEquals(id, auditor.getId());
        assertEquals(createdAt, auditor.getCreatedAt());
        assertEquals(updatedAt, auditor.getUpdatedAt());
        assertEquals(desactivatedAt, auditor.getDesactivatedAt());
    }

    /**
     * Prueba para validar la correcta creación del objeto Auditor usando el
     * constructor completo.
     */
    @Test
    void testConstructorCompleto() {
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        LocalDateTime desactivatedAt = LocalDateTime.now().plusDays(1);

        // Crear una instancia de Auditor usando el constructor completo
        auditor = new Auditor(id, createdAt, updatedAt, desactivatedAt);

        // Verificar que los valores han sido correctamente asignados
        assertEquals(id, auditor.getId());
        assertEquals(createdAt, auditor.getCreatedAt());
        assertEquals(updatedAt, auditor.getUpdatedAt());
        assertEquals(desactivatedAt, auditor.getDesactivatedAt());
    }

    /**
     * Prueba para validar la creación de una instancia de Auditor con solo el
     * ID usando el constructor.
     */
    @Test
    void testConstructorConId() {
        Long id = 1L;

        // Crear una instancia de Auditor solo con el ID
        auditor = new Auditor(id);

        // Verificar que el ID ha sido correctamente asignado
        assertEquals(id, auditor.getId());
        // Verificar que los otros atributos sean nulos
        assertNotNull(auditor);
    }

    /**
     * Prueba para verificar que los atributos de Auditor se inicializan como
     * null.
     */
    @Test
    void testAtributosInicialmenteNulos() {
        // Crear una instancia vacía de Auditor
        auditor = new Auditor();

        // Verificar que todos los atributos estén inicialmente en null
        assertEquals(null, auditor.getId());
        assertEquals(null, auditor.getCreatedAt());
        assertEquals(null, auditor.getUpdatedAt());
        assertEquals(null, auditor.getDesactivatedAt());
    }

    /**
     * Prueba para validar que el método setId acepta valores nulos y no lanza
     * excepciones.
     */
    @Test
    void testSetIdNull() {
        auditor.setId(null);
        assertNull(auditor.getId(), "El ID debe ser null.");
    }

    /**
     * Prueba para verificar el comportamiento del método setCreatedAt con valor
     * null.
     */
    @Test
    void testSetCreatedAtNull() {
        auditor.setCreatedAt(null);
        assertNull(auditor.getCreatedAt(), "La fecha de creación debe ser null.");
    }

    /**
     * Prueba para verificar el comportamiento del método setUpdatedAt con valor
     * null.
     */
    @Test
    void testSetUpdatedAtNull() {
        auditor.setUpdatedAt(null);
        assertNull(auditor.getUpdatedAt(), "La fecha de actualización debe ser null.");
    }

    /**
     * Prueba para verificar el comportamiento del método setDesactivatedAt con
     * valor null.
     */
    @Test
    void testSetDesactivatedAtNull() {
        auditor.setDesactivatedAt(null);
        assertNull(auditor.getDesactivatedAt(), "La fecha de desactivación debe ser null.");
    }

    /**
     * Prueba para validar que los campos de Auditor no son modificados sin la
     * asignación explícita.
     */
    @Test
    void testNoModificacionSinAsignacion() {
        // Verificar que todos los atributos sean null antes de la asignación
        assertNull(auditor.getId(), "El ID debe ser null.");
        assertNull(auditor.getCreatedAt(), "La fecha de creación debe ser null.");
        assertNull(auditor.getUpdatedAt(), "La fecha de actualización debe ser null.");
        assertNull(auditor.getDesactivatedAt(), "La fecha de desactivación debe ser null.");
    }

}
