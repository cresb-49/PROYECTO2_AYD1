package usac.api.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ServicioMasDemandadoDto;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteServiciosImprimible;
import usac.api.repositories.ReservaRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.tools.ManejadorTiempo;

class ReporteServiciosMasSolicitadosTest {

    @InjectMocks
    private ReporteServiciosMasSolicitados reporteServiciosMasSolicitados;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReporteServiciosImprimible reporteServiciosImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    private ReporteRequest reporteRequest;
    private List<ServicioMasDemandadoDto> servicios;
    private List<ServicioMasDemandadoDto> canchas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración del rango de fechas
        reporteRequest = new ReporteRequest();
        reporteRequest.setFecha1(LocalDate.parse("2023-01-01"));
        reporteRequest.setFecha2(LocalDate.parse("2023-01-31"));

        // Datos simulados para servicios y canchas más demandados
        servicios = Arrays.asList(
                new ServicioMasDemandadoDto(1L, "Servicio1", 10L),
                new ServicioMasDemandadoDto(2L, "Servicio2", 8L)
        );

        canchas = Arrays.asList(
                new ServicioMasDemandadoDto(3L, "Cancha1", 5L),
                new ServicioMasDemandadoDto(4L, "Cancha2", 7L)
        );

        when(reservaRepository.findServiciosMasDemandados(Date.valueOf(reporteRequest.getFecha1()), Date.valueOf(reporteRequest.getFecha2())))
                .thenReturn(servicios);
        when(reservaRepository.findCanchasMasDemandadas(Date.valueOf(reporteRequest.getFecha1()), Date.valueOf(reporteRequest.getFecha2())))
                .thenReturn(canchas);

        when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class)))
                .thenReturn("01/01/2023", "31/01/2023");
    }

    @Test
    void testGenerarServiciosFrecuentes() {
        ReporteServiciosDTO reporte = reporteServiciosMasSolicitados.generarServiciosFrecuentes(reporteRequest);

        assertNotNull(reporte);
        assertEquals(2, reporte.getServicios().size());
        assertEquals(2, reporte.getCanchas().size());
        assertEquals("01/01/2023", reporte.getFecha1());
        assertEquals("31/01/2023", reporte.getFecha2());
        assertEquals(4, reporte.getNumeroReservaciones());
    }

    @Test
    void testExportarServiciosFrecuentes() throws Exception {
        ReporteExportRequest exportRequest = new ReporteExportRequest();
        exportRequest.setFecha1(LocalDate.parse("2023-01-01"));
        exportRequest.setFecha2(LocalDate.parse("2023-01-31"));
        exportRequest.setTipoExporte("pdf");

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(reporteServiciosImprimible.init(any(ReporteServiciosDTO.class), eq("pdf"))).thenReturn(expectedBytes);

        byte[] resultBytes = reporteServiciosMasSolicitados.exportarServiciosFrecuentes(exportRequest);

        assertNotNull(resultBytes);
        assertArrayEquals(expectedBytes, resultBytes);
        verify(reporteServiciosImprimible, times(1)).init(any(ReporteServiciosDTO.class), eq("pdf"));
    }
}
