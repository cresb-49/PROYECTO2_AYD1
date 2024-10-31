package usac.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import usac.api.models.*;
import usac.api.models.dto.ArchivoDTO;
import usac.api.models.request.ReservacionCanchaRequest;
import usac.api.models.request.ReservacionServicioRequest;
import usac.api.repositories.ReservaRepository;
import usac.api.repositories.ReservaCanchaRepository;
import usac.api.repositories.ReservaServicioRepository;
import usac.api.tools.ManejadorTiempo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import usac.api.reportes.imprimibles.ComprobanteReservaImprimible;
import usac.api.reportes.imprimibles.FacturaImprimible;

class ReservaServiceTest {

    @InjectMocks
    private ReservaService reservaService;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaCanchaRepository reservaCanchaRepository;

    @Mock
    private ReservaServicioRepository reservaServicioRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private ManejadorTiempo manejadorFechas;

    @Mock
    private HorarioCanchaService horarioCanchaService;

    @Mock
    private DiaService diaService;

    @Mock
    private CanchaService canchaService;

    @Mock
    private NegocioService negocioService;

    @Mock
    private ServicioService servicioService;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private FacturaService facturaService;

    @Mock
    private ComprobanteReservaImprimible comprobanteReservaImprimible;

    @Mock
    private FacturaImprimible facturaImprimible;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private javax.validation.Validator validator;

    @Test
    void testReservaServicio_SinDisponibilidadDeEmpleados() throws Exception {
        // Arrange
        ReservacionServicioRequest request = new ReservacionServicioRequest();
        request.setServicioId(1L);
        request.setFechaReservacion(LocalDate.now());
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(LocalTime.of(12, 0));

        Servicio servicio = new Servicio();
        servicio.setEmpleadosParalelos(1);
        when(servicioService.getServicioById(1L)).thenReturn(servicio);
        when(negocioService.obtenerNegocio()).thenReturn(new Negocio(20.0));

        // Mock la no disponibilidad de empleados
        when(reservaServicioRepository.countReservasActivasPorServicio(anyLong(), any(), any(), any())).thenReturn(1);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> reservaService.reservaServicio(request));
        assertFalse(exception.getMessage().contains(
                "No hay disponibilidad de empleados para "
                + "este servicio en el horario seleccionado."));
    }

    @Test
    void testRealizarLaReserva_ReservaYaRealizada() throws Exception {
        // Arrange
        Long reservaId = 1L;
        Reserva reserva = new Reserva();
        reserva.setRealizada(true);
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            reservaService.realizarLaReserva(reservaId);
        });

        assertTrue(exception.getMessage().contains("La reservacion ya ha sido realizada"));
    }

    @Test
    void testObtenerReservaPorId_ThrowsExceptionIfNotFound() {
        // Arrange
        Long reservaId = 1L;
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            reservaService.obtenerReservaPorId(reservaId);
        });

        assertTrue(exception.getMessage().contains("No se encontró la reserva"));
    }
}
