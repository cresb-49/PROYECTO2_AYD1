package usac.api.reportes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.DisponiblilidadRecursoDTO;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.imprimibles.ReporteDisponibilidadImprimible;
import usac.api.repositories.ReservaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.reportes.ReporteDisponibilidad;
import usac.api.tools.ManejadorTiempo;

class ReporteDisponibilidadTest {

    @InjectMocks
    private ReporteDisponibilidad reporteDisponibilidad;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReporteDisponibilidadImprimible reporteDisponibilidadImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    private ReporteRequest reporteRequest;
    private List<DisponiblilidadRecursoDTO> empleados;
    private List<DisponiblilidadRecursoDTO> canchas;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuración del rango de fechas
        reporteRequest = new ReporteRequest();
        reporteRequest.setFecha1(LocalDate.parse("2023-01-01"));
        reporteRequest.setFecha2(LocalDate.parse("2023-01-31"));

        // Configuración de datos simulados para empleados y canchas
        empleados = Arrays.asList(
                new DisponiblilidadRecursoDTO("Empleado1", LocalTime.of(9, 0), LocalTime.of(11, 0), LocalDate.parse("2023-01-02"), "Recurso1"),
                new DisponiblilidadRecursoDTO("Empleado2", LocalTime.of(10, 0), LocalTime.of(12, 0), LocalDate.parse("2023-01-15"), "Recurso2")
        );

        canchas = Arrays.asList(
                new DisponiblilidadRecursoDTO("Cancha1", LocalTime.of(14, 0), LocalTime.of(16, 0), LocalDate.parse("2023-01-10"), "Recurso3"),
                new DisponiblilidadRecursoDTO("Cancha2", LocalTime.of(15, 0), LocalTime.of(17, 0), LocalDate.parse("2023-01-20"), "Recurso4")
        );

        when(reservaRepository.findReporteDisponibilidadReservasServicio(reporteRequest.getFecha1(), reporteRequest.getFecha2()))
                .thenReturn(empleados);
        when(reservaRepository.findReporteDisponibilidadReservasCancha(reporteRequest.getFecha1(), reporteRequest.getFecha2()))
                .thenReturn(canchas);

        when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class)))
                .thenReturn("02/01/2023", "15/01/2023", "10/01/2023", "20/01/2023", "01/01/2023", "31/01/2023");
    }

    @Test
    void testGenerarReporteDisponiblilidad() {
        ReporteDisponibilidadDTO reporte = reporteDisponibilidad.generarReporteDisponiblilidad(reporteRequest);

        assertNotNull(reporte);
        assertEquals(2, reporte.getDisponibilidadEmpleados().size());
        assertEquals(2, reporte.getDisponibilidadCanchas().size());
        assertEquals("02/01/2023", reporte.getDisponibilidadEmpleados().get(0).getFechaReservacionStr());
        assertEquals("10/01/2023", reporte.getDisponibilidadCanchas().get(0).getFechaReservacionStr());
        assertEquals("01/01/2023", reporte.getFecha1());
        assertEquals("31/01/2023", reporte.getFecha2());
    }

    @Test
    void testExportarReporteDisponiblilidad() throws Exception {
        ReporteExportRequest exportRequest = new ReporteExportRequest();
        exportRequest.setFecha1(LocalDate.parse("2023-01-01"));
        exportRequest.setFecha2(LocalDate.parse("2023-01-31"));
        exportRequest.setTipoExporte("pdf");

        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(reporteDisponibilidadImprimible.init(any(ReporteDisponibilidadDTO.class), eq("pdf"))).thenReturn(expectedBytes);

        byte[] resultBytes = reporteDisponibilidad.exportarReporteDisponiblilidad(exportRequest);

        assertNotNull(resultBytes);
        assertArrayEquals(expectedBytes, resultBytes);
        verify(reporteDisponibilidadImprimible, times(1)).init(any(ReporteDisponibilidadDTO.class), eq("pdf"));
    }
}
