package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Factura;
import usac.api.models.Reserva;
import usac.api.models.Usuario;
import usac.api.services.NegocioService;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import usac.api.models.Negocio;

class FacturaImprimibleTest {

    @InjectMocks
    private FacturaImprimible facturaImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private Factura mockFactura;

    @Mock
    private Reserva mockReserva;

    @Mock
    private Usuario mockUsuario;

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configurar datos del mock de Factura y sus métodos relacionados
            when(mockFactura.getId()).thenReturn(1L);
            when(mockFactura.getTotal()).thenReturn(1500.0);
            when(mockFactura.getNombre()).thenReturn("Juan Perez");
            when(mockFactura.getNit()).thenReturn("1234567");
            when(mockFactura.getConcepto()).thenReturn("Servicio de consulta");
            when(mockFactura.getCreatedAt()).thenReturn(LocalDateTime.of(2024, 1, 15, 10, 30));
            when(mockFactura.getReserva()).thenReturn(mockReserva);

            // Configurar datos del mock de Reserva y Usuario
            when(mockReserva.getAdelanto()).thenReturn(500.0);
            when(mockReserva.getReservador()).thenReturn(mockUsuario);
            when(mockReserva.getTotalACobrar()).thenReturn(1500.0);

            // Configurar datos del mock de Usuario
            when(mockUsuario.getCui()).thenReturn("987654321");

            // Configurar comportamiento de manejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");

            // Configurar comportamiento de tiendaConfigService
            when(tiendaConfigService.obtenerNegocio()).thenReturn(
                    new Negocio("asdasda", "asdasd", true, "dasd",
                            2.3)); // Configura según tus necesidades
        } catch (Exception ex) {
            Logger.getLogger(FacturaImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "PDF";
        byte[] resultado = facturaImprimible.init(mockFactura, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockFactura, atLeastOnce()).getId();
        verify(mockFactura, atLeastOnce()).getTotal();
        verify(mockReserva, atLeastOnce()).getAdelanto();
    }
}
