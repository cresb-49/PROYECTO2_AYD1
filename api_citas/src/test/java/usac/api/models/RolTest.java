package usac.api.models;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

/**
 * Clase de pruebas unitarias para la clase Rol. Se validan los atributos de la
 * clase y las restricciones definidas en el modelo.
 */
public class RolTest {

    @InjectMocks
    private Rol rol;

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
     * Prueba para validar que se lanza una excepción cuando el nombre del rol
     * está vacío.
     */
    @Test
    void testNombreRolVacio() {
        // Crear una instancia de Rol con el nombre vacío
        rol = new Rol("");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Rol>> violations = Arrays.asList(validator.validate(rol).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del rol no puede estar vacío.")));
    }

    /**
     * Prueba para validar que se lanza una excepción cuando el nombre del rol
     * es nulo.
     */
    @Test
    void testNombreRolNulo() {
        // Crear una instancia de Rol con el nombre nulo
        rol = new Rol(null);
        // Validar las restricciones del modelo
        List<ConstraintViolation<Rol>> violations = Arrays.asList(validator.validate(rol).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del rol no puede ser nulo")));
    }

    /**
     * Prueba para validar que el nombre del rol tiene un valor correcto
     * (CLIENTE, ADMIN o AYUDANTE).
     */
    @Test
    void testNombreRolValorCorrecto() {
        // Crear una instancia de Rol con un nombre correcto
        rol = new Rol("ADMIN");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Rol>> violations = Arrays.asList(validator.validate(rol).toArray(new ConstraintViolation[0]));

        // Verificar que no existen violaciones a las restricciones
        assertTrue(violations.isEmpty());
    }

    /**
     * Prueba para validar que se lanza una excepción cuando el nombre del rol
     * tiene un valor incorrecto.
     */
    @Test
    void testNombreRolValorIncorrecto() {
        // Crear una instancia de Rol con un nombre incorrecto
        rol = new Rol("INVALIDO");
        // Validar las restricciones del modelo
        List<ConstraintViolation<Rol>> violations = Arrays.asList(validator.validate(rol).toArray(new ConstraintViolation[0]));

        // Verificar que se lanza la excepción adecuada
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre de rol solo puede ser CLIENTE, ADMIN, AYUDANTE")));
    }

    /**
     * Prueba para verificar el correcto funcionamiento de los setters y getters
     * de la clase Rol.
     */
    @Test
    void testSettersYGetters() {
        // Crear una instancia de Rol y establecer atributos
        rol = new Rol();
        rol.setNombre("CLIENTE");

        // Verificar que los valores se establecen y obtienen correctamente
        assertEquals("CLIENTE", rol.getNombre());
    }

    /**
     * Prueba para validar que la lista de usuarios con el rol se inicializa
     * como null.
     */
    @Test
    void testUsuariosConElRolInicialmenteNull() {
        // Crear una instancia de Rol
        rol = new Rol();

        // Verificar que la lista de usuarios con el rol es inicialmente null
        assertNull(rol.getUsuariosConElRol());
    }

    /**
     * Prueba para verificar que se puede asignar y recuperar correctamente la
     * lista de usuarios con el rol.
     */
    @Test
    void testSetAndGetUsuariosConElRol() {
        // Crear una instancia de Rol
        rol = new Rol();

        // Crear una lista de usuarios asociados al rol
        List<RolUsuario> usuariosConRol = new ArrayList<>();
        RolUsuario rolUsuario1 = new RolUsuario();
        RolUsuario rolUsuario2 = new RolUsuario();
        usuariosConRol.add(rolUsuario1);
        usuariosConRol.add(rolUsuario2);

        // Establecer la lista de usuarios en el rol
        rol.setUsuariosConElRol(usuariosConRol);

        // Verificar que los valores se establecen y recuperan correctamente
        assertEquals(2, rol.getUsuariosConElRol().size());
        assertEquals(rolUsuario1, rol.getUsuariosConElRol().get(0));
        assertEquals(rolUsuario2, rol.getUsuariosConElRol().get(1));
    }

    /**
     * Prueba para validar que no se permite asignar un nombre de rol vacío.
     */
    @Test
    void testNombreRolVacioNoPermitido() {
        // Crear una instancia de Rol con nombre vacío
        rol = new Rol("");

        // Validar restricciones del modelo
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));

        // Verificar que existe una violación de la restricción de nombre no vacío
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del rol no puede estar vacío.")));
    }

    /**
     * Prueba para validar que no se permite asignar un nombre de rol con
     * caracteres especiales.
     */
    @Test
    void testNombreRolConCaracteresEspecialesNoPermitido() {
        // Crear una instancia de Rol con nombre incorrecto
        rol = new Rol("ADM!N");

        // Validar restricciones del modelo
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));

        // Verificar que existe una violación de la restricción de formato correcto para el nombre del rol
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El nombre de rol solo puede ser CLIENTE, ADMIN, AYUDANTE")));
    }

}
