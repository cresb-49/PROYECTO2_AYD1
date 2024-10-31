
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.models.request.ReporteExportRequest;
import usac.api.models.request.ReporteRequest;
import usac.api.reportes.ReporteCientesFrecuentes;
import usac.api.reportes.ReporteDisponibilidad;
import usac.api.reportes.ReporteServiciosMasSolicitados;
import usac.api.reportes.ReporteVentas;

import javax.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.services.ReporteService;

class ReporteServiceTest {

    @InjectMocks
    private ReporteService reporteService;

    @Mock
    private ReporteVentas reporteVentas;

    @Mock
    private ReporteCientesFrecuentes reporteCientesFrecuentes;

    @Mock
    private ReporteServiciosMasSolicitados reporteServiciosMasSolicitados;

    @Mock
    private ReporteDisponibilidad reporteDisponibilidad;

    @Mock
    private Validator validator;  // Agregar el Validator mock

    private ReporteExportRequest exportRequest;
    private ReporteRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simular validación exitosa
        when(validator.validate(any())).thenReturn(new HashSet<>());

        exportRequest = new ReporteExportRequest();
        exportRequest.setTipoReporte("reporteVentas");
        exportRequest.setTipoExporte("pdf");

        request = new ReporteRequest();
    }

    @Test
    void testExportarReporteVentas() throws Exception {
        byte[] expectedBytes = new byte[]{1, 2, 3};
        when(reporteVentas.exportarReporteVentas(any(ReporteExportRequest.class))).thenReturn(expectedBytes);

        ArchivoDTO archivoDTO = reporteService.exportarReporte(exportRequest);

        assertNotNull(archivoDTO);
        assertArrayEquals(expectedBytes, archivoDTO.getArchivo());
        assertEquals("application/pdf", archivoDTO.getHeaders().getContentType().toString());
        verify(reporteVentas, times(1)).exportarReporteVentas(any(ReporteExportRequest.class));
    }

    @Test
    void testExportarReporteClientes() throws Exception {
        exportRequest.setTipoReporte("reporteClientes");
        byte[] expectedBytes = new byte[]{4, 5, 6};
        when(reporteCientesFrecuentes.exportarClientesFrecuentes(any(ReporteExportRequest.class))).thenReturn(expectedBytes);

        ArchivoDTO archivoDTO = reporteService.exportarReporte(exportRequest);

        assertNotNull(archivoDTO);
        assertArrayEquals(expectedBytes, archivoDTO.getArchivo());
        verify(reporteCientesFrecuentes, times(1)).exportarClientesFrecuentes(any(ReporteExportRequest.class));
    }

    @Test
    void testExportarReporteServicios() throws Exception {
        exportRequest.setTipoReporte("reporteServicios");
        byte[] expectedBytes = new byte[]{7, 8, 9};
        when(reporteServiciosMasSolicitados.exportarServiciosFrecuentes(any(ReporteExportRequest.class))).thenReturn(expectedBytes);

        ArchivoDTO archivoDTO = reporteService.exportarReporte(exportRequest);

        assertNotNull(archivoDTO);
        assertArrayEquals(expectedBytes, archivoDTO.getArchivo());
        verify(reporteServiciosMasSolicitados, times(1)).exportarServiciosFrecuentes(any(ReporteExportRequest.class));
    }

    @Test
    void testExportarReporteDisponibilidad() throws Exception {
        exportRequest.setTipoReporte("reporteDisponibilidad");
        byte[] expectedBytes = new byte[]{10, 11, 12};
        when(reporteDisponibilidad.exportarReporteDisponiblilidad(any(ReporteExportRequest.class))).thenReturn(expectedBytes);

        ArchivoDTO archivoDTO = reporteService.exportarReporte(exportRequest);

        assertNotNull(archivoDTO);
        assertArrayEquals(expectedBytes, archivoDTO.getArchivo());
        verify(reporteDisponibilidad, times(1)).exportarReporteDisponiblilidad(any(ReporteExportRequest.class));
    }

    @Test
    void testSetHeadersPdf() {
        HttpHeaders headers = reporteService.setHeaders(FileHttpMetaData.PDF);

        assertNotNull(headers);
        assertEquals("application/pdf", headers.getContentType().toString());
    }

    @Test
    void testSetHeadersExcel() {
        HttpHeaders headers = reporteService.setHeaders(FileHttpMetaData.EXCEL);

        assertNotNull(headers);
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", headers.getContentType().toString());
    }

    @Test
    void testGetReporteVentas() throws Exception {
        ReporteVentasDTO reporteVentasDTO = new ReporteVentasDTO();
        when(reporteVentas.generarReporteVentas(any(ReporteRequest.class))).thenReturn(reporteVentasDTO);

        ReporteVentasDTO result = reporteService.getReporteVentas(request);

        assertNotNull(result);
        verify(reporteVentas, times(1)).generarReporteVentas(any(ReporteRequest.class));
    }

    @Test
    void testGetReporteClientesFrecuentes() throws Exception {
        ReporteClientesFrecuentesDto reporteClientesFrecuentesDto = new ReporteClientesFrecuentesDto();
        when(reporteCientesFrecuentes.generarCientesFrecuentes(any(ReporteRequest.class))).thenReturn(reporteClientesFrecuentesDto);

        ReporteClientesFrecuentesDto result = reporteService.getReporteClientesFrecuentes(request);

        assertNotNull(result);
        verify(reporteCientesFrecuentes, times(1)).generarCientesFrecuentes(any(ReporteRequest.class));
    }

    @Test
    void testGetReporteServicios() throws Exception {
        ReporteServiciosDTO reporteServiciosDTO = new ReporteServiciosDTO();
        when(reporteServiciosMasSolicitados.generarServiciosFrecuentes(any(ReporteRequest.class))).thenReturn(reporteServiciosDTO);

        ReporteServiciosDTO result = reporteService.getReporteServicios(request);

        assertNotNull(result);
        verify(reporteServiciosMasSolicitados, times(1)).generarServiciosFrecuentes(any(ReporteRequest.class));
    }

    @Test
    void testGetReporteDisponibilidad() throws Exception {
        ReporteDisponibilidadDTO reporteDisponibilidadDTO = new ReporteDisponibilidadDTO();
        when(reporteDisponibilidad.generarReporteDisponiblilidad(any(ReporteRequest.class))).thenReturn(reporteDisponibilidadDTO);

        ReporteDisponibilidadDTO result = reporteService.getReporteDisponibilidad(request);

        assertNotNull(result);
        verify(reporteDisponibilidad, times(1)).generarReporteDisponiblilidad(any(ReporteRequest.class));
    }
}
