package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.services.ReporteService;
import usac.api.services.permisos.ValidadorPermiso;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReportesControllerTest {

    @InjectMocks
    private ReportesController reportesController;

    @Mock
    private ReporteService reporteService;

    @Mock
    private ValidadorPermiso validadorPermiso;

    @Mock
    private ArchivoDTO mockArchivoDTO;

    @Mock
    private ReporteVentasDTO mockReporteVentasDTO;

    @Mock
    private ReporteClientesFrecuentesDto mockReporteClientesFrecuentesDto;

    @Mock
    private ReporteServiciosDTO mockReporteServiciosDTO;

    @Mock
    private ReporteDisponibilidadDTO mockReporteDisponibilidadDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportarReporte() {
        try {
            // Setup
            ReporteExportRequest request = new ReporteExportRequest();
            when(reporteService.exportarReporte(request)).thenReturn(mockArchivoDTO);

            // Execute
            var response = reportesController.exportarReporte(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reporteService, times(1)).exportarReporte(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testExportarReporteError() {
        try {
            // Setup
            ReporteExportRequest request = new ReporteExportRequest();
            when(reporteService.exportarReporte(request)).thenThrow(new RuntimeException("Error al exportar"));

            // Execute
            var response = reportesController.exportarReporte(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testReporteVentas() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteVentas(request)).thenReturn(mockReporteVentasDTO);

            // Execute
            var response = reportesController.reporteVentas(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reporteService, times(1)).getReporteVentas(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testReporteVentasError() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteVentas(request)).thenThrow(new RuntimeException("Error al generar reporte de ventas"));

            // Execute
            var response = reportesController.reporteVentas(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testReporteClientes() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteClientesFrecuentes(request)).thenReturn(mockReporteClientesFrecuentesDto);

            // Execute
            var response = reportesController.reporteClientes(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reporteService, times(1)).getReporteClientesFrecuentes(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testReporteClientesError() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteClientesFrecuentes(request)).thenThrow(new RuntimeException("Error al generar reporte de clientes"));

            // Execute
            var response = reportesController.reporteClientes(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testServiciosMasSolicitados() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteServicios(request)).thenReturn(mockReporteServiciosDTO);

            // Execute
            var response = reportesController.serviciosMasSolicitados(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reporteService, times(1)).getReporteServicios(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testServiciosMasSolicitadosError() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteServicios(request)).thenThrow(new RuntimeException("Error al generar reporte de servicios"));

            // Execute
            var response = reportesController.serviciosMasSolicitados(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testDisponibilidadRecursos() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteDisponibilidad(request)).thenReturn(mockReporteDisponibilidadDTO);

            // Execute
            var response = reportesController.disponiblilidadRecursos(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
            verify(reporteService, times(1)).getReporteDisponibilidad(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void testDisponibilidadRecursosError() {
        try {
            // Setup
            ReporteRequest request = new ReporteRequest();
            when(reporteService.getReporteDisponibilidad(request)).thenThrow(new RuntimeException("Error al generar reporte de disponibilidad"));

            // Execute
            var response = reportesController.disponiblilidadRecursos(request);

            // Verify
            assertNotNull(response);
            verify(validadorPermiso, times(1)).verificarPermiso();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
