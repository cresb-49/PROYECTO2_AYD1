package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolUsuarioTest {

    private RolUsuario rolUsuario;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        rol = new Rol();
        rolUsuario = new RolUsuario(usuario, rol);
    }

    @Test
    void testConstructor() {
        assertNotNull(rolUsuario);
        assertEquals(usuario, rolUsuario.getUsuario());
        assertEquals(rol, rolUsuario.getRol());
    }

    @Test
    void testSettersAndGetters() {
        Usuario nuevoUsuario = new Usuario();
        Rol nuevoRol = new Rol();

        rolUsuario.setUsuario(nuevoUsuario);
        rolUsuario.setRol(nuevoRol);

        assertEquals(nuevoUsuario, rolUsuario.getUsuario());
        assertEquals(nuevoRol, rolUsuario.getRol());
    }

    @Test
    void testUsuarioNotNull() {
        assertNotNull(rolUsuario.getUsuario());
        assertEquals(usuario, rolUsuario.getUsuario());
    }

    @Test
    void testRolNotNull() {
        assertNotNull(rolUsuario.getRol());
        assertEquals(rol, rolUsuario.getRol());
    }
}
