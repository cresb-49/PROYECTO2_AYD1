package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.ReporteServiciosDTO;
import usac.api.services.NegocioService;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReporteServiciosImprimibleTest {

    @InjectMocks
    private ReporteServiciosImprimible reporteServiciosImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private ReporteServiciosDTO mockReporteServiciosDTO;

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configuración del mock de ReporteServiciosDTO
            when(mockReporteServiciosDTO.getNumeroReservaciones()).thenReturn(10);
            when(mockReporteServiciosDTO.getFecha1()).thenReturn("01/01/2024");
            when(mockReporteServiciosDTO.getFecha2()).thenReturn("31/01/2024");
            when(mockReporteServiciosDTO.getServicios()).thenReturn(new ArrayList<>()); // Lista vacía o con datos simulados
            when(mockReporteServiciosDTO.getCanchas()).thenReturn(new ArrayList<>()); // Lista vacía o con datos simulados

            // Configurar comportamiento de manejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");

            // Configurar comportamiento de tiendaConfigService
            when(tiendaConfigService.obtenerNegocio()).thenReturn(
                    new usac.api.models.Negocio("ghjk", "Dirección Tienda", true, "logoBase64", 1.0)); // Configura según tus necesidades

        } catch (Exception ex) {
            Logger.getLogger(ReporteServiciosImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "PDF";
        byte[] resultado = reporteServiciosImprimible.init(mockReporteServiciosDTO, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockReporteServiciosDTO, atLeastOnce()).getNumeroReservaciones();
        verify(mockReporteServiciosDTO, atLeastOnce()).getFecha1();
        verify(mockReporteServiciosDTO, atLeastOnce()).getFecha2();
    }
}
