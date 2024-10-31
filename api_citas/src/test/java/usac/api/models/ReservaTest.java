package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaTest {

    private Reserva reserva;
    private Usuario mockUsuario;
    private Factura mockFactura;
    private ReservaCancha mockReservaCancha;
    private ReservaServicio mockReservaServicio;

    @BeforeEach
    void setUp() {
        // Crear instancias mock
        mockUsuario = mock(Usuario.class);
        mockFactura = mock(Factura.class);
        mockReservaCancha = mock(ReservaCancha.class);
        mockReservaServicio = mock(ReservaServicio.class);

        // Inicializar la instancia de prueba
        reserva = new Reserva(
                mockUsuario,
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                LocalDate.of(2024, 1, 1),
                mockFactura,
                true,
                500.0,
                1000.0
        );
    }

    @Test
    void testConstructorInicializaCamposCorrectamente() {
        assertEquals(mockUsuario, reserva.getReservador());
        assertEquals(LocalTime.of(9, 0), reserva.getHoraInicio());
        assertEquals(LocalTime.of(11, 0), reserva.getHoraFin());
        assertEquals(LocalDate.of(2024, 1, 1), reserva.getFechaReservacion());
        assertEquals(mockFactura, reserva.getFactura());
        assertTrue(reserva.getRealizada());
        assertEquals(500.0, reserva.getAdelanto());
        assertEquals(1000.0, reserva.getTotalACobrar());
    }

    @Test
    void testSetReservador() {
        Usuario nuevoUsuario = mock(Usuario.class);
        reserva.setReservador(nuevoUsuario);
        assertEquals(nuevoUsuario, reserva.getReservador());
    }

    @Test
    void testSetHoraInicio() {
        reserva.setHoraInicio(LocalTime.of(10, 0));
        assertEquals(LocalTime.of(10, 0), reserva.getHoraInicio());
    }

    @Test
    void testSetHoraFin() {
        reserva.setHoraFin(LocalTime.of(12, 0));
        assertEquals(LocalTime.of(12, 0), reserva.getHoraFin());
    }

    @Test
    void testSetFechaReservacion() {
        reserva.setFechaReservacion(LocalDate.of(2024, 2, 2));
        assertEquals(LocalDate.of(2024, 2, 2), reserva.getFechaReservacion());
    }

    @Test
    void testSetFactura() {
        Factura nuevaFactura = mock(Factura.class);
        reserva.setFactura(nuevaFactura);
        assertEquals(nuevaFactura, reserva.getFactura());
    }

    @Test
    void testSetRealizada() {
        reserva.setRealizada(false);
        assertFalse(reserva.getRealizada());
    }

    @Test
    void testSetReservaCancha() {
        reserva.setReservaCancha(mockReservaCancha);
        assertEquals(mockReservaCancha, reserva.getReservaCancha());
    }

    @Test
    void testSetReservaServicio() {
        reserva.setReservaServicio(mockReservaServicio);
        assertEquals(mockReservaServicio, reserva.getReservaServicio());
    }

    @Test
    void testSetCanceledAt() {
        LocalDateTime fechaCancelacion = LocalDateTime.of(2024, 1, 15, 15, 30);
        reserva.setCanceledAt(fechaCancelacion);
        assertEquals(fechaCancelacion, reserva.getCanceledAt());
    }

    @Test
    void testSetAdelanto() {
        reserva.setAdelanto(600.0);
        assertEquals(600.0, reserva.getAdelanto());
    }

    @Test
    void testSetTotalACobrar() {
        reserva.setTotalACobrar(1200.0);
        assertEquals(1200.0, reserva.getTotalACobrar());
    }
}
