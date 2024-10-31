package usac.api.models.dto.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteClientesFrecuentesDtoTest {

    private ReporteClientesFrecuentesDto reporteClientes;
    private List<ClienteFrecuenteDto> clienteFrecuentes;

    @BeforeEach
    void setUp() {
        ClienteFrecuenteDto cliente1 = new ClienteFrecuenteDto(1L, "Juan Perez", 10L, 2000.0, 200.0);
        ClienteFrecuenteDto cliente2 = new ClienteFrecuenteDto(2L, "Ana Lopez", 5L, 1000.0, 200.0);
        clienteFrecuentes = Arrays.asList(cliente1, cliente2);

        reporteClientes = new ReporteClientesFrecuentesDto(clienteFrecuentes, 15, 3000.0, "2024-01-01", "2024-12-31");
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(clienteFrecuentes, reporteClientes.getClienteFrecuentes());
        assertEquals(15, reporteClientes.getNumeroVentas());
        assertEquals(3000.0, reporteClientes.getTotalVentas());
        assertEquals("2024-01-01", reporteClientes.getFecha1());
        assertEquals("2024-12-31", reporteClientes.getFecha2());
    }

    @Test
    void testSetClienteFrecuentes() {
        ClienteFrecuenteDto nuevoCliente = new ClienteFrecuenteDto(3L, "Carlos Mendez", 8L, 1600.0, 200.0);
        List<ClienteFrecuenteDto> nuevosClientes = Arrays.asList(nuevoCliente);
        reporteClientes.setClienteFrecuentes(nuevosClientes);
        assertEquals(nuevosClientes, reporteClientes.getClienteFrecuentes());
    }

    @Test
    void testSetNumeroVentas() {
        reporteClientes.setNumeroVentas(20);
        assertEquals(20, reporteClientes.getNumeroVentas());
    }

    @Test
    void testSetTotalVentas() {
        reporteClientes.setTotalVentas(4000.0);
        assertEquals(4000.0, reporteClientes.getTotalVentas());
    }

    @Test
    void testSetFecha1() {
        reporteClientes.setFecha1("2025-01-01");
        assertEquals("2025-01-01", reporteClientes.getFecha1());
    }

    @Test
    void testSetFecha2() {
        reporteClientes.setFecha2("2025-12-31");
        assertEquals("2025-12-31", reporteClientes.getFecha2());
    }
}
