package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservaCanchaTest {

    private ReservaCancha reservaCancha;
    private Reserva reserva;
    private Cancha cancha;

    @BeforeEach
    void setUp() {
        reserva = new Reserva();
        cancha = new Cancha();
        reservaCancha = new ReservaCancha(reserva, cancha);
    }

    @Test
    void testConstructor() {
        assertNotNull(reservaCancha);
        assertEquals(reserva, reservaCancha.getReserva());
        assertEquals(cancha, reservaCancha.getCancha());
    }

    @Test
    void testSettersAndGetters() {
        Reserva nuevaReserva = new Reserva();
        Cancha nuevaCancha = new Cancha();

        reservaCancha.setReserva(nuevaReserva);
        reservaCancha.setCancha(nuevaCancha);

        assertEquals(nuevaReserva, reservaCancha.getReserva());
        assertEquals(nuevaCancha, reservaCancha.getCancha());
    }

    @Test
    void testCanchaNotNull() {
        assertNotNull(reservaCancha.getCancha());
        assertEquals(cancha, reservaCancha.getCancha());
    }

    @Test
    void testReservaNotNull() {
        assertNotNull(reservaCancha.getReserva());
        assertEquals(reserva, reservaCancha.getReserva());
    }
}
