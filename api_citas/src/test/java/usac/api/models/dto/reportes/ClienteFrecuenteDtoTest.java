package usac.api.models.dto.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class ClienteFrecuenteDtoTest {

    private ClienteFrecuenteDto cliente;

    @BeforeEach
    void setUp() {
        cliente = new ClienteFrecuenteDto(1L, "Juan Perez", 5L, 1500.0, 300.0);
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(1L, cliente.getId());
        assertEquals("Juan Perez", cliente.getNombre());
        assertEquals(5L, cliente.getNumeroReservaciones());
        assertEquals(1500.0, cliente.getValorTotalCompras());
        assertEquals(300.0, cliente.getTicketPromedio());
    }

    @Test
    void testConstructorInicializaStringsCorrectamente() {
        assertEquals("Q 1500.0", cliente.getValorTotalComprasStr());
        assertEquals("Q 300.0", cliente.getTicketPromedioStr());
    }

    @Test
    void testSetNombre() {
        cliente.setNombre("Carlos");
        assertEquals("Carlos", cliente.getNombre());
    }

    @Test
    void testSetNumeroReservaciones() {
        cliente.setNumeroReservaciones(10L);
        assertEquals(10L, cliente.getNumeroReservaciones());
    }

    @Test
    void testSetValorTotalCompras() {
        cliente.setValorTotalCompras(2000.0);
        assertEquals(2000.0, cliente.getValorTotalCompras());
    }

    @Test
    void testSetTicketPromedio() {
        cliente.setTicketPromedio(400.0);
        assertEquals(400.0, cliente.getTicketPromedio());
    }

    @Test
    void testSetValorTotalComprasStr() {
        cliente.setValorTotalComprasStr("Q 2000.0");
        assertEquals("Q 2000.0", cliente.getValorTotalComprasStr());
    }

    @Test
    void testSetTicketPromedioStr() {
        cliente.setTicketPromedioStr("Q 400.0");
        assertEquals("Q 400.0", cliente.getTicketPromedioStr());
    }
    
    @Test
    void testSetNumeroPedidos() {
        cliente.setNumeroPedidos(7L);
        assertEquals(7L, cliente.getNumeroPedidos());
    }
    
    @Test
    void testSetId() {
        cliente.setId(2L);
        assertEquals(2L, cliente.getId());
    }
}
