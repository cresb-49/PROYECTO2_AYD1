package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Negocio;
import usac.api.models.dto.reportes.ReporteClientesFrecuentesDto;
import usac.api.services.NegocioService;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReporteClientesImprimibleTest {

    @InjectMocks
    private ReporteClientesImprimible reporteClientesImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private ReporteClientesFrecuentesDto mockClientesFrecuentesDto;

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configuración del mock de ReporteClientesFrecuentesDto
            when(mockClientesFrecuentesDto.getTotalVentas()).thenReturn(2000.0);
            when(mockClientesFrecuentesDto.getNumeroVentas()).thenReturn(50);
            when(mockClientesFrecuentesDto.getFecha1()).thenReturn("01/01/2024");
            when(mockClientesFrecuentesDto.getFecha2()).thenReturn("31/01/2024");
            when(mockClientesFrecuentesDto.getClienteFrecuentes()).thenReturn(new ArrayList<>());

            // Configuración del mock de ManejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");

            // Configuración del mock de NegocioService
            Negocio mockNegocio = new Negocio("rdtfyguhjk", "Direccion Tienda", true, "logoBase64", 1.0);
            when(tiendaConfigService.obtenerNegocio()).thenReturn(mockNegocio);

        } catch (Exception ex) {
            Logger.getLogger(ReporteClientesImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "img";
        byte[] resultado = reporteClientesImprimible.init(mockClientesFrecuentesDto, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockClientesFrecuentesDto, atLeastOnce()).getTotalVentas();
        verify(mockClientesFrecuentesDto, atLeastOnce()).getNumeroVentas();
    }
}
