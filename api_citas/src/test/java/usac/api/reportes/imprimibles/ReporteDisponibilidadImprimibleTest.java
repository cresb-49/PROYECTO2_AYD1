package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.dto.reportes.ReporteDisponibilidadDTO;
import usac.api.services.NegocioService;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReporteDisponibilidadImprimibleTest {

    @InjectMocks
    private ReporteDisponibilidadImprimible reporteDisponibilidadImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private ReporteDisponibilidadDTO mockReporteDisponibilidadDTO;

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configuración del mock de ReporteDisponibilidadDTO
            when(mockReporteDisponibilidadDTO.getFecha1()).thenReturn("01/01/2024");
            when(mockReporteDisponibilidadDTO.getFecha2()).thenReturn("31/01/2024");
            when(mockReporteDisponibilidadDTO.getDisponibilidadEmpleados()).thenReturn(new ArrayList<>()); // Lista vacía o con datos simulados
            when(mockReporteDisponibilidadDTO.getDisponibilidadCanchas()).thenReturn(new ArrayList<>()); // Lista vacía o con datos simulados

            // Configurar comportamiento de manejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");

            // Configurar comportamiento de tiendaConfigService
            when(tiendaConfigService.obtenerNegocio()).thenReturn(
                    new usac.api.models.Negocio("sadfasdfasd", "Dirección Tienda", true, "logoBase64", 1.0)); // Configura según tus necesidades

        } catch (Exception ex) {
            Logger.getLogger(ReporteDisponibilidadImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "PDF";
        byte[] resultado = reporteDisponibilidadImprimible.init(mockReporteDisponibilidadDTO, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockReporteDisponibilidadDTO, atLeastOnce()).getFecha1();
        verify(mockReporteDisponibilidadDTO, atLeastOnce()).getFecha2();
    }
}
