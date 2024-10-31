package usac.api.models.dto.reportes;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServicioMasDemandadoDtoTest {

    private ServicioMasDemandadoDto servicio;

    @BeforeEach
    void setUp() {
        servicio = new ServicioMasDemandadoDto(1L, "Consulta General", 100L);
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(1L, servicio.getId());
        assertEquals("Consulta General", servicio.getNombre());
        assertEquals(100L, servicio.getCantidadReservas());
    }

    @Test
    void testSetId() {
        servicio.setId(2L);
        assertEquals(2L, servicio.getId());
    }

    @Test
    void testSetNombre() {
        servicio.setNombre("Limpieza Dental");
        assertEquals("Limpieza Dental", servicio.getNombre());
    }

    @Test
    void testSetCantidadReservas() {
        servicio.setCantidadReservas(200L);
        assertEquals(200L, servicio.getCantidadReservas());
    }
}
