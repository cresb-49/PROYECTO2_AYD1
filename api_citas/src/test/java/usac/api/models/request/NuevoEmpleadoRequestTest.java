package usac.api.models.request;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import usac.api.models.Rol;
import usac.api.models.Usuario;

class NuevoEmpleadoRequestTest {

    private NuevoEmpleadoRequest nuevoEmpleadoRequest;
    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        // Inicializar los objetos para el test
        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        rol = new Rol("EMPLEADO");

        // Inicializar NuevoEmpleadoRequest con los objetos
        nuevoEmpleadoRequest = new NuevoEmpleadoRequest(usuario, new ArrayList<>(), rol);
    }

    @Test
    void testConstructor() {
        // Verificar que el constructor asigna correctamente los valores
        assertNotNull(nuevoEmpleadoRequest.getUsuario());
        assertEquals("test@example.com", nuevoEmpleadoRequest.getUsuario().getEmail());
    }

    @Test
    void testSetUsuario() {
        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("nuevo@example.com");

        // Asignar el nuevo usuario
        nuevoEmpleadoRequest.setUsuario(nuevoUsuario);

        // Verificar que el usuario fue actualizado correctamente
        assertEquals(nuevoUsuario, nuevoEmpleadoRequest.getUsuario());
        assertEquals("nuevo@example.com", nuevoEmpleadoRequest.getUsuario().getEmail());
    }

    @Test
    void testConstructorSinParametros() {
        // Verificar que el constructor vacío no inicializa los valores
        NuevoEmpleadoRequest requestVacio = new NuevoEmpleadoRequest(null, null, null);
        assertNull(requestVacio.getUsuario());
        assertNull(requestVacio.getRol());
    }
}
