package usac.api.reportes.imprimibles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Reserva;
import usac.api.models.Usuario;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import usac.api.models.Negocio;
import usac.api.services.NegocioService;

class ComprobanteReservaImprimibleTest {

    @InjectMocks
    private ComprobanteReservaImprimible comprobanteReservaImprimible;

    @Mock
    private ManejadorTiempo manejadorTiempo;

    @Mock
    private Reserva mockReserva;

    @Mock
    private Usuario mockUsuario; // Suponiendo que Reserva tiene un Usuario como reservador

    @Mock
    private NegocioService tiendaConfigService; // Añadir mock para tiendaConfigService

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);

            // Configurar datos del mock de Reserva
            when(mockReserva.getId()).thenReturn(1L);
            when(mockReserva.getReservador()).thenReturn(mockUsuario);
            when(mockReserva.getAdelanto()).thenReturn(500.0);
            when(mockReserva.getRealizada()).thenReturn(true);
            when(mockReserva.getFechaReservacion()).thenReturn(LocalDate.now());
            when(mockReserva.getHoraInicio()).thenReturn(LocalTime.of(10, 0));
            when(mockReserva.getHoraFin()).thenReturn(LocalTime.of(11, 0));

            // Configurar datos del mock de Usuario
            when(mockUsuario.getNombres()).thenReturn("Juan");
            when(mockUsuario.getApellidos()).thenReturn("Pérez");
            when(mockUsuario.getNit()).thenReturn("1234567");
            when(mockUsuario.getCui()).thenReturn("987654321");

            // Configurar comportamiento de manejadorTiempo
            when(manejadorTiempo.parsearFechaYHoraAFormatoRegional(any(LocalDate.class))).thenReturn("15/01/2024");
            when(tiendaConfigService.obtenerNegocio()).thenReturn(
                    new Negocio("asdasda", "asdasd", true, "dasd",
                            2.3));
        } catch (Exception ex) {
            Logger.getLogger(ComprobanteReservaImprimibleTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    void testInit() throws Exception {
        // Ejecutar el método init
        String tipoExportacion = "word";
        byte[] resultado = comprobanteReservaImprimible.init(mockReserva, tipoExportacion);

        // Verificar que el resultado no es nulo y tiene contenido
        assertNotNull(resultado, "El resultado de la exportación no debe ser nulo");
        verify(mockReserva, atLeastOnce()).getId();
        verify(mockReserva, atLeastOnce()).getAdelanto();
        verify(mockReserva, atLeastOnce()).getReservador();
        verify(mockUsuario, atLeastOnce()).getNombres();
    }
}
