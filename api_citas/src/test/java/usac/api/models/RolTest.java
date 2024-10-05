package usac.api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

/**
 * Clase de pruebas unitarias para la clase Rol.
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
     * Prueba para validar que el nombre del rol no puede estar vacío.
     */
    @Test
    void testNombreRolVacio() {
        rol = new Rol("");
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del rol no puede estar vacío.")));
    }

    /**
     * Prueba para validar que el nombre del rol no puede ser nulo.
     */
    @Test
    void testNombreRolNulo() {
        String x = null;
        rol = new Rol(x);
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre del rol no puede ser nulo")));
    }

    /**
     * Prueba para validar que se lanza una excepción cuando el nombre del rol
     * tiene un valor incorrecto.
     */
    @Test
    void testNombreRolValorIncorrecto() {
        rol = new Rol("INVALIDO");
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre de rol solo puede ser CLIENTE, ADMIN, AYUDANTE")));
    }

    /**
     * Prueba para validar que se permite un nombre de rol válido (CLIENTE,
     * ADMIN o AYUDANTE).
     */
    @Test
    void testNombreRolValorCorrecto() {
        rol = new Rol("CLIENTE");
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));
        assertTrue(violations.isEmpty());
    }

    /**
     * Prueba para validar que no se permite asignar un nombre de rol con
     * caracteres especiales.
     */
    @Test
    void testNombreRolConCaracteresEspecialesNoPermitido() {
        rol = new Rol("ADM!N");
        List<ConstraintViolation<Rol>> violations = new ArrayList<>(validator.validate(rol));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("El nombre de rol solo puede ser CLIENTE, ADMIN, AYUDANTE")));
    }

    /**
     * Prueba para verificar que los atributos de la clase Rol se inicializan
     * correctamente con el constructor.
     */
    @Test
    void testConstructorConId() {
        rol = new Rol(1L);
        assertEquals(1L, rol.getId());
    }

    /**
     * Prueba para validar que la lista de usuarios con el rol se inicializa
     * como null.
     */
    @Test
    void testUsuariosConElRolInicialmenteNull() {
        rol = new Rol();
        assertNull(rol.getUsusarios());
    }

    /**
     * Prueba para validar que la lista de permisos del rol se inicializa como
     * null.
     */
    @Test
    void testPermisosRolInicialmenteNull() {
        rol = new Rol();
        assertNull(rol.getPermisosRol());
    }

    /**
     * Prueba para verificar que se puede asignar y recuperar correctamente la
     * lista de usuarios con el rol.
     */
    @Test
    void testSetAndGetUsuarios() {
        // Crear un nuevo objeto de Rol
        Rol rol = new Rol();

        // Crear una lista de usuarios y agregar algunos usuarios
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        // Establecer la lista de usuarios en el rol
        rol.setUsusarios(usuarios);

        // Verificar que se han establecido los usuarios correctamente
        assertEquals(2, rol.getUsusarios().size());
    }

    /**
     * Prueba para verificar que se puede asignar y recuperar correctamente la
     * lista de permisos del rol.
     */
    @Test
    void testSetAndGetPermisosRol() {
        rol = new Rol();
        List<RolPermiso> permisosRol = new ArrayList<>();
        RolPermiso permiso1 = new RolPermiso();
        RolPermiso permiso2 = new RolPermiso();
        permisosRol.add(permiso1);
        permisosRol.add(permiso2);
        rol.setPermisosRol(permisosRol);
        assertEquals(2, rol.getPermisosRol().size());
    }

    /**
     * Prueba para verificar el correcto funcionamiento de los setters y getters
     * de la clase Rol.
     */
    @Test
    void testSettersYGetters() {
        rol = new Rol();
        rol.setNombre("CLIENTE");
        assertEquals("CLIENTE", rol.getNombre());
    }
}
