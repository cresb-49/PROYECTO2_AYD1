package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.ReporteVentasDTO;
import usac.api.services.NegocioService;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReporteVentasImprimibleTest {

    @InjectMocks
    private ReporteVentasImprimible reporteVentasImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private ReporteVentasDTO mockReporteVentasDTO;

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configuración del mock de ReporteVentasDTO
            when(mockReporteVentasDTO.getTotal()).thenReturn(5000.0);
            when(mockReporteVentasDTO.getNoVentas()).thenReturn(100);
            when(mockReporteVentasDTO.getTotalAdelantos()).thenReturn(1000.0);
            when(mockReporteVentasDTO.getTotalNoAdelanto()).thenReturn(4000.0);
            when(mockReporteVentasDTO.getFecha1()).thenReturn("01/01/2024");
            when(mockReporteVentasDTO.getFecha2()).thenReturn("31/01/2024");
            when(mockReporteVentasDTO.getFacturas()).thenReturn(new ArrayList<>()); // Lista vacía o con datos simulados

            // Configurar comportamiento de manejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");

            // Configurar comportamiento de tiendaConfigService
            when(tiendaConfigService.obtenerNegocio()).thenReturn(
                    new usac.api.models.Negocio("asdfsdafsa", "Dirección Tienda", true, "logoBase64", 1.0)); // Configura según tus necesidades

        } catch (Exception ex) {
            Logger.getLogger(ReporteVentasImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "excel";
        byte[] resultado = reporteVentasImprimible.init(mockReporteVentasDTO, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockReporteVentasDTO, atLeastOnce()).getTotal();
        verify(mockReporteVentasDTO, atLeastOnce()).getNoVentas();
        verify(mockReporteVentasDTO, atLeastOnce()).getTotalAdelantos();
        verify(mockReporteVentasDTO, atLeastOnce()).getTotalNoAdelanto();
        verify(mockReporteVentasDTO, atLeastOnce()).getFecha1();
        verify(mockReporteVentasDTO, atLeastOnce()).getFecha2();
    }
}
