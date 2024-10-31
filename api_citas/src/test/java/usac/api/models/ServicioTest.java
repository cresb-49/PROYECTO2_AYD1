package usac.api.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServicioTest {

    private Servicio servicio;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(); // Suponiendo que Rol tiene un constructor vacío
        servicio = new Servicio("Servicio Test", 1.5, "imagen.jpg", 100.0,
                "Detalles del servicio", rol, 3);
    }

    @Test
    void testConstructorAndAttributes() {
        assertEquals("Servicio Test", servicio.getNombre());
        assertEquals(1.5, servicio.getDuracion());
        assertEquals("imagen.jpg", servicio.getImagen());
        assertEquals(100.0, servicio.getCosto());
        assertEquals("Detalles del servicio", servicio.getDetalles());
        assertEquals(rol, servicio.getRol());
        assertEquals(3, servicio.getEmpleadosParalelos());
    }

    @Test
    void testSetNombre() {
        servicio.setNombre("Nuevo Nombre");
        assertEquals("Nuevo Nombre", servicio.getNombre());
    }

    @Test
    void testSetDuracion() {
        servicio.setDuracion(2.0);
        assertEquals(2.0, servicio.getDuracion());
    }

    @Test
    void testSetImagen() {
        servicio.setImagen("nueva_imagen.jpg");
        assertEquals("nueva_imagen.jpg", servicio.getImagen());
    }

    @Test
    void testSetCosto() {
        servicio.setCosto(150.0);
        assertEquals(150.0, servicio.getCosto());
    }

    @Test
    void testSetDetalles() {
        servicio.setDetalles("Nuevos detalles del servicio");
        assertEquals("Nuevos detalles del servicio", servicio.getDetalles());
    }

    @Test
    void testSetRol() {
        Rol nuevoRol = new Rol();
        servicio.setRol(nuevoRol);
        assertEquals(nuevoRol, servicio.getRol());
    }

    @Test
    void testSetEmpleadosParalelos() {
        servicio.setEmpleadosParalelos(5);
        assertEquals(5, servicio.getEmpleadosParalelos());
    }

    @Test
    void testSetReservas() {
        List<ReservaServicio> reservas = new ArrayList<>();
        servicio.setReservas(reservas);
        assertEquals(reservas, servicio.getReservas());
    }

    @Test
    void testToString() {
        String expected = "Servicio [nombre=Servicio Test, duracion=1.5, costo=100.0, detalles=Detalles del servicio, rol=" + rol + "]";
        assertEquals(expected, servicio.toString());
    }
}
