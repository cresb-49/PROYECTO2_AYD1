package usac.api.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.ClienteFrecuenteDto;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteClientesImprimible;
import usac.api.repositories.FacturaRepository;
import usac.api.tools.ManejadorTiempo;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReporteCientesFrecuentesTest {

    @InjectMocks
    private ReporteCientesFrecuentes reporteCientesFrecuentes;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private ReporteClientesImprimible reporteClientesImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    private ReporteRequest reporteRequest;
    private List<ClienteFrecuenteDto> clientesFrecuentes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración de fechas para el reporte
        reporteRequest = new ReporteRequest();
        reporteRequest.setFecha1(LocalDate.parse("2024-01-01"));
        reporteRequest.setFecha2(LocalDate.parse("2024-12-31"));

        // Configuración de datos simulados para clientes frecuentes
        clientesFrecuentes = Arrays.asList(
                new ClienteFrecuenteDto(1L, "Cliente1", 10L, 500.0, 50.0),
                new ClienteFrecuenteDto(2L, "Cliente2", 5L, 300.0, 60.0)
        );

        // Configuramos el mock para `facturaRepository` y `manejadorTiempo`
        when(facturaRepository.findClientesFrecuentes(Date.valueOf(reporteRequest.getFecha1()),
                Date.valueOf(reporteRequest.getFecha2())))
                .thenReturn(clientesFrecuentes);

        when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class)))
                .thenReturn("01/01/2024", "31/12/2024");
    }

    @Test
    void testGenerarCientesFrecuentes() {
        ReporteClientesFrecuentesDto resultado = reporteCientesFrecuentes.generarCientesFrecuentes(reporteRequest);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(2, resultado.getClienteFrecuentes().size());
        assertEquals(800.0, resultado.getTotalVentas());
        assertEquals("01/01/2024", resultado.getFecha1());
        assertEquals("31/12/2024", resultado.getFecha2());

        // Verificar que se llamó al repositorio y al manejador de tiempo
        verify(facturaRepository, times(1))
                .findClientesFrecuentes(Date.valueOf(reporteRequest.getFecha1()), Date.valueOf(reporteRequest.getFecha2()));
        verify(manejadorTiempo, times(2)).parsearFechaYHoraAFormatoRegional(any(LocalDate.class));
    }

    @Test
    void testExportarClientesFrecuentes() throws Exception {
        // Configuramos el `ReporteExportRequest` para exportar el reporte en formato PDF
        ReporteExportRequest exportRequest = new ReporteExportRequest();
        exportRequest.setFecha1(LocalDate.parse("2024-01-01"));
        exportRequest.setFecha2(LocalDate.parse("2024-12-31"));
        exportRequest.setTipoExporte("pdf");

        // Configuramos el mock para el `ReporteClientesImprimible`
        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(reporteClientesImprimible.init(any(ReporteClientesFrecuentesDto.class), eq("pdf"))).thenReturn(expectedBytes);

        // Ejecutamos el método
        byte[] resultBytes = reporteCientesFrecuentes.exportarClientesFrecuentes(exportRequest);

        // Verificación de los resultados
        assertNotNull(resultBytes);
        assertArrayEquals(expectedBytes, resultBytes);
        verify(reporteClientesImprimible, times(1)).init(any(ReporteClientesFrecuentesDto.class), eq("pdf"));
    }
}
