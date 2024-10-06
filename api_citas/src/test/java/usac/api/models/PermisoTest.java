package usac.api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

/**
 * Clase de pruebas unitarias para la clase Permiso. Se validan los atributos y
 * las restricciones definidas en el modelo.
 */
public class PermisoTest {

    @InjectMocks
    private Permiso permiso;

    private Validator validator;

    /**
     * Configuración inicial antes de cada prueba. Se inicializa un validador
     * para aplicar las restricciones del modelo.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Prueba para validar que se lanza una excepción cuando el nombre del
     * permiso está vacío.
     */
    @Test
    void testNombrePermisoVacio() {
        // Crear una instancia de Permiso con el nombre vacío
        permiso = new Permiso("", "");
        permiso.setRuta("ruta/valida");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Permiso>> violations = Arrays.asList(validator.validate(permiso).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del permiso no puede estar vacío.")));
    }

    /**
     * Prueba para validar que se lanza una excepción cuando el nombre del
     * permiso es nulo.
     */
    @Test
    void testNombrePermisoNulo() {
        // Crear una instancia de Permiso con el nombre nulo
        permiso = new Permiso(null, "ruta/valida");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Permiso>> violations = Arrays.asList(validator.validate(permiso).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del permiso no puede ser nulo")));
    }

    /**
     * Prueba para validar que se lanza una excepción cuando la ruta del permiso
     * está vacía.
     */
    @Test
    void testRutaPermisoVacia() {
        // Crear una instancia de Permiso con la ruta vacía
        permiso = new Permiso("Permiso1", "");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Permiso>> violations = Arrays.asList(validator.validate(permiso).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del permiso no puede estar vacío.")));
    }

    /**
     * Prueba para validar que se lanza una excepción cuando la ruta del permiso
     * es nula.
     */
    @Test
    void testRutaPermisoNula() {
        // Crear una instancia de Permiso con la ruta nula
        permiso = new Permiso("Permiso1", null);
        // Validar las restricciones del modelo
        List<ConstraintViolation<Permiso>> violations = Arrays.asList(validator.validate(permiso).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del permiso no puede ser nulo")));
    }

    /**
     * Prueba para verificar el correcto funcionamiento de los setters y getters
     * de la clase Permiso.
     */
    @Test
    void testSettersYGetters() {
        // Crear una instancia de Permiso y establecer atributos
        permiso = new Permiso();
        permiso.setNombre("Permiso1");
        permiso.setRuta("ruta/permiso");

        // Verificar que los valores se establecen y obtienen correctamente
        assertEquals("Permiso1", permiso.getNombre());
        assertEquals("ruta/permiso", permiso.getRuta());
    }

    /**
     * Prueba para validar que la lista de asignaciones de permiso se inicializa
     * como null.
     */
    @Test
    void testAsignacionesInicialmenteNull() {
        // Crear una instancia de Permiso
        permiso = new Permiso();

        // Verificar que la lista de asignaciones es inicialmente null
        assertNull(permiso.getAsignaciones());
    }

    /**
     * Prueba para verificar que se puede asignar y recuperar correctamente la
     * lista de asignaciones.
     */
    @Test
    void testSetAndGetAsignaciones() {
        // Crear una instancia de Permiso
        permiso = new Permiso();

        // Crear una lista de asignaciones
        List<RolPermiso> asignaciones = Arrays.asList(new RolPermiso(), new RolPermiso());

        // Establecer la lista de asignaciones
        permiso.setAsignaciones(asignaciones);

        // Verificar que los valores se establecen y recuperan correctamente
        assertEquals(2, permiso.getAsignaciones().size());
    }
}
