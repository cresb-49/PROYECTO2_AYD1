package usac.api.models.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.TipoEmpleado;
import usac.api.models.Usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class NuevoEmpleadoRequestTest {

    private NuevoEmpleadoRequest nuevoEmpleadoRequest;
    private Usuario usuario;
    private TipoEmpleado tipoEmpleado;

    @BeforeEach
    void setUp() {
        // Inicializar los objetos para el test
        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        tipoEmpleado = new TipoEmpleado();
        tipoEmpleado.setNombre("Gerente");

        // Inicializar NuevoEmpleadoRequest con los objetos
        nuevoEmpleadoRequest = new NuevoEmpleadoRequest(usuario, tipoEmpleado);
    }

    @Test
    void testConstructor() {
        // Verificar que el constructor asigna correctamente los valores
        assertNotNull(nuevoEmpleadoRequest.getUsuario());
        assertEquals("test@example.com", nuevoEmpleadoRequest.getUsuario().getEmail());
        assertNotNull(nuevoEmpleadoRequest.getTipoEmpleado());
        assertEquals("Gerente", nuevoEmpleadoRequest.getTipoEmpleado().getNombre());
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
    void testSetTipoEmpleado() {
        // Crear un nuevo tipo de empleado
        TipoEmpleado nuevoTipoEmpleado = new TipoEmpleado();
        nuevoTipoEmpleado.setNombre("Supervisor");

        // Asignar el nuevo tipo de empleado
        nuevoEmpleadoRequest.setTipoEmpleado(nuevoTipoEmpleado);

        // Verificar que el tipo de empleado fue actualizado correctamente
        assertEquals(nuevoTipoEmpleado, nuevoEmpleadoRequest.getTipoEmpleado());
        assertEquals("Supervisor", nuevoEmpleadoRequest.getTipoEmpleado().getNombre());
    }

    @Test
    void testConstructorSinParametros() {
        // Verificar que el constructor vacío no inicializa los valores
        NuevoEmpleadoRequest requestVacio = new NuevoEmpleadoRequest(null, null);
        assertNull(requestVacio.getUsuario());
        assertNull(requestVacio.getTipoEmpleado());
    }
}
