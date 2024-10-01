/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Luis Monterroso
 */
public class RolUsuarioTest {

    private RolUsuario rolUsuario;
    private Rol rol;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configuramos un objeto Rol y un objeto Usuario antes de cada prueba
        rol = new Rol();
        rol.setNombre("ADMIN");

        usuario = new Usuario();
        usuario.setEmail("test@example.com");

        // Instanciamos el objeto RolUsuario con los objetos rol y usuario
        rolUsuario = new RolUsuario(rol, usuario);
    }

    @Test
    void testRolUsuarioConstructor() {
        // Verificar que el constructor asigna correctamente los valores
        assertNotNull(rolUsuario.getRol());
        assertEquals("ADMIN", rolUsuario.getRol().getNombre());

        assertNotNull(rolUsuario.getUsuario());
        assertEquals("test@example.com", rolUsuario.getUsuario().getEmail());
    }

    @Test
    void testGetRol() {
        // Verificar que getRol retorna correctamente el rol asignado
        assertEquals(rol, rolUsuario.getRol());
    }

    @Test
    void testSetRol() {
        // Crear un nuevo rol para asignarlo
        Rol nuevoRol = new Rol();
        nuevoRol.setNombre("USER");

        // Asignar el nuevo rol
        rolUsuario.setRol(nuevoRol);

        // Verificar que el rol fue actualizado correctamente
        assertEquals(nuevoRol, rolUsuario.getRol());
        assertEquals("USER", rolUsuario.getRol().getNombre());
    }

    @Test
    void testGetUsuario() {
        // Verificar que getUsuario retorna correctamente el usuario asignado
        assertEquals(usuario, rolUsuario.getUsuario());
    }

    @Test
    void testSetUsuario() {
        // Crear un nuevo usuario para asignarlo
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("newuser@example.com");

        // Asignar el nuevo usuario
        rolUsuario.setUsuario(nuevoUsuario);

        // Verificar que el usuario fue actualizado correctamente
        assertEquals(nuevoUsuario, rolUsuario.getUsuario());
        assertEquals("newuser@example.com", rolUsuario.getUsuario().getEmail());
    }

    @Test
    void testRolUsuarioDefaultConstructor() {
        // Verificar que el constructor vacío inicializa un objeto RolUsuario sin valores
        RolUsuario nuevoRolUsuario = new RolUsuario();
        assertNull(nuevoRolUsuario.getRol());
        assertNull(nuevoRolUsuario.getUsuario());
    }
}
