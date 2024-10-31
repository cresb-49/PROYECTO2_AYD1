package usac.api.models.dto.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.Factura;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReporteVentasDTOTest {

    private ReporteVentasDTO reporteVentas;
    private List<Factura> facturas;

    @BeforeEach
    void setUp() {
        Factura factura1 = new Factura("Cliente A", "123456", "Servicio X", "Detalle del servicio X", 200.0);
        Factura factura2 = new Factura("Cliente B", "789012", "Servicio Y", "Detalle del servicio Y", 350.0);
        facturas = Arrays.asList(factura1, factura2);

        reporteVentas = new ReporteVentasDTO(550.0, 2, 150.0, 400.0, facturas, "2024-01-01", "2024-12-31");
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(550.0, reporteVentas.getTotal());
        assertEquals(2, reporteVentas.getNoVentas());
        assertEquals(150.0, reporteVentas.getTotalAdelantos());
        assertEquals(400.0, reporteVentas.getTotalNoAdelanto());
        assertEquals(facturas, reporteVentas.getFacturas());
        assertEquals("2024-01-01", reporteVentas.getFecha1());
        assertEquals("2024-12-31", reporteVentas.getFecha2());
    }

    @Test
    void testSetTotal() {
        reporteVentas.setTotal(600.0);
        assertEquals(600.0, reporteVentas.getTotal());
    }

    @Test
    void testSetNoVentas() {
        reporteVentas.setNoVentas(3);
        assertEquals(3, reporteVentas.getNoVentas());
    }

    @Test
    void testSetTotalAdelantos() {
        reporteVentas.setTotalAdelantos(200.0);
        assertEquals(200.0, reporteVentas.getTotalAdelantos());
    }

    @Test
    void testSetTotalNoAdelanto() {
        reporteVentas.setTotalNoAdelanto(450.0);
        assertEquals(450.0, reporteVentas.getTotalNoAdelanto());
    }

    @Test
    void testSetFacturas() {
        Factura nuevaFactura = new Factura("Cliente C", "345678", "Servicio Z", "Detalle del servicio Z", 500.0);
        List<Factura> nuevasFacturas = Arrays.asList(nuevaFactura);
        reporteVentas.setFacturas(nuevasFacturas);
        assertEquals(nuevasFacturas, reporteVentas.getFacturas());
    }

    @Test
    void testSetFecha1() {
        reporteVentas.setFecha1("2025-01-01");
        assertEquals("2025-01-01", reporteVentas.getFecha1());
    }

    @Test
    void testSetFecha2() {
        reporteVentas.setFecha2("2025-12-31");
        assertEquals("2025-12-31", reporteVentas.getFecha2());
    }
}
