/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luis Monterroso
 */
public class UsuarioTest {

    private Validator validator;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        usuario = new Usuario();
        usuario.setNit("123456");
        usuario.setCui("1234567890123");
        usuario.setPhone("12345678");
        usuario.setEmail("test@example.com");
        usuario.setNombres("Juan");
        usuario.setApellidos("Perez");
        usuario.setPassword("password123");
    }

    @Test
    void testUsuarioValido() {
        // Verificar que no hay errores de validación en un usuario válido
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNitInvalido() {
        // Probar un Nit inválido
        usuario.setNit("12");  // Nit demasiado corto
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nit del cliente debe tener entre 6 y 12 caracteres.")));
    }

    @Test
    void testCuiInvalido() {
        // Probar un CUI inválido
        usuario.setCui("123");  // CUI demasiado corto
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El cui del cliente debe tener 13 caracteres.")));
    }

    @Test
    void testPhoneInvalido() {
        // Probar un teléfono inválido
        usuario.setPhone("123");  // Teléfono demasiado corto
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El teléfono del cliente debe tener 8 digitos")));
    }

    @Test
    void testEmailInvalido() {
        // Probar un email inválido
        usuario.setEmail("");  // Email vacío
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El email del cliente no puede estar vacío.")));
    }

    @Test
    void testNombresInvalido() {
        // Probar nombres inválidos
        usuario.setNombres("");  // Nombres vacíos
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Los nombres del cliente no puede estar vacío.")));
    }

    @Test
    void testApellidosInvalido() {
        // Probar apellidos inválidos
        usuario.setApellidos("");  // Apellidos vacíos
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Los apellidos del cliente no puede estar vacío.")));
    }

    @Test
    void testPasswordInvalido() {
        // Probar password inválido
        usuario.setPassword("");  // Password vacía
        Set<ConstraintViolation<Usuario>> violations = validator.validate(usuario);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La password del cliente no puede estar vacía.")));
    }

    @Test
    void testRolAsignadoAlUsuario() {
        // Crear un rol y asignarlo al usuario
        Rol rol = new Rol();
        rol.setNombre("CLIENTE");

        // Crear una lista de usuarios y agregar algunos usuarios
        List<RolUsuario> roles = new ArrayList<>();
        roles.add(new RolUsuario(null, rol));

        usuario.setRoles(roles);

        // Verificar que el rol fue asignado correctamente
        assertNotNull(usuario.getRoles());
    }
}
