package usac.api.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Factura;
import usac.api.models.Reserva;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteVentasImprimible;
import usac.api.repositories.FacturaRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.tools.ManejadorTiempo;

class ReporteVentasTest {

    @InjectMocks
    private ReporteVentas reporteVentas;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private ReporteVentasImprimible reporteVentasImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    private ReporteRequest reporteRequest;
    private List<Factura> facturas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración del rango de fechas
        reporteRequest = new ReporteRequest();
        reporteRequest.setFecha1(LocalDate.of(2023, 1, 1));
        reporteRequest.setFecha2(LocalDate.of(2023, 1, 31));

        // Configuración de facturas simuladas
        facturas = Arrays.asList(
                crearFactura(100.0, 20.0),
                crearFactura(150.0, null),
                crearFactura(200.0, 50.0)
        );

        when(facturaRepository.findAllByCreatedAtDateBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(facturas);
        when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class)))
                .thenReturn("01/01/2023", "31/01/2023");
    }

    private Factura crearFactura(Double total, Double adelanto) {
        Factura factura = new Factura();
        factura.setTotal(total);
        Reserva reserva = new Reserva();
        reserva.setAdelanto(adelanto);
        factura.setReserva(reserva);
        return factura;
    }

    @Test
    void testGenerarReporteVentas() {
        ReporteVentasDTO reporte = reporteVentas.generarReporteVentas(reporteRequest);

        assertNotNull(reporte);
        assertEquals(450.0, reporte.getTotal());
        assertEquals(3, reporte.getNoVentas());
        assertEquals(70.0, reporte.getTotalAdelantos());
        assertEquals(380.0, reporte.getTotalNoAdelanto());
        assertEquals("01/01/2023", reporte.getFecha1());
        assertEquals("31/01/2023", reporte.getFecha2());
    }

    @Test
    void testExportarReporteVentas() throws Exception {
        ReporteExportRequest exportRequest = new ReporteExportRequest();
        exportRequest.setFecha1(LocalDate.of(2023, 1, 1));
        exportRequest.setFecha2(LocalDate.of(2023, 1, 31));
        exportRequest.setTipoExporte("pdf");

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(reporteVentasImprimible.init(any(ReporteVentasDTO.class), eq("pdf"))).thenReturn(expectedBytes);

        byte[] resultBytes = reporteVentas.exportarReporteVentas(exportRequest);

        assertNotNull(resultBytes);
        assertArrayEquals(expectedBytes, resultBytes);
        verify(reporteVentasImprimible, times(1)).init(any(ReporteVentasDTO.class), eq("pdf"));
    }
}
