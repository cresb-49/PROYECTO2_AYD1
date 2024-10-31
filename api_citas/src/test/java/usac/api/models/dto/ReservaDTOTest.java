package usac.api.models.dto;

import org.junit.jupiter.api.Test;
import usac.api.models.ReservaCancha;
import usac.api.models.ReservaServicio;
import usac.api.models.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaDTOTest {

    /**
     * Prueba para verificar la inicialización del constructor completo.
     */
    @Test
    void testConstructorCompleto() {
        Long id = 1L;
        Usuario reservador = new Usuario();
        LocalTime horaInicio = LocalTime.of(10, 0);
        LocalTime horaFin = LocalTime.of(11, 0);
        LocalDate fechaReservacion = LocalDate.of(2024, 10, 15);
        Long idFactura = 100L;
        Boolean realizada = true;
        LocalDateTime canceledAt = LocalDateTime.of(2024, 10, 15, 9, 0);
        Double adelanto = 50.0;
        Double totalACobrar = 100.0;
        ReservaCancha reservaCancha = new ReservaCancha();
        ReservaServicio reservaServicio = new ReservaServicio();

        ReservaDTO reservaDTO = new ReservaDTO(id, reservador, horaInicio, horaFin, fechaReservacion, idFactura,
                realizada, canceledAt, adelanto, totalACobrar, reservaCancha, reservaServicio);

        assertEquals(id, reservaDTO.getId());
        assertEquals(reservador, reservaDTO.getReservador());
        assertEquals(horaInicio, reservaDTO.getHoraInicio());
        assertEquals(horaFin, reservaDTO.getHoraFin());
        assertEquals(fechaReservacion, reservaDTO.getFechaReservacion());
        assertEquals(idFactura, reservaDTO.getIdFactura());
        assertEquals(realizada, reservaDTO.getRealizada());
        assertEquals(canceledAt, reservaDTO.getCanceledAt());
        assertEquals(adelanto, reservaDTO.getAdelanto());
        assertEquals(totalACobrar, reservaDTO.getTotalACobrar());
        assertEquals(reservaCancha, reservaDTO.getReservaCancha());
        assertEquals(reservaServicio, reservaDTO.getReservaServicio());
    }

    /**
     * Prueba para verificar la inicialización del constructor vacío.
     */
    @Test
    void testConstructorVacio() {
        ReservaDTO reservaDTO = new ReservaDTO();

        assertNull(reservaDTO.getId());
        assertNull(reservaDTO.getReservador());
        assertNull(reservaDTO.getHoraInicio());
        assertNull(reservaDTO.getHoraFin());
        assertNull(reservaDTO.getFechaReservacion());
        assertNull(reservaDTO.getIdFactura());
        assertNull(reservaDTO.getRealizada());
        assertNull(reservaDTO.getCanceledAt());
        assertNull(reservaDTO.getAdelanto());
        assertNull(reservaDTO.getTotalACobrar());
        assertNull(reservaDTO.getReservaCancha());
        assertNull(reservaDTO.getReservaServicio());
    }

    /**
     * Pruebas de getter y setter para cada atributo de ReservaDTO.
     */
    @Test
    void testGetSetId() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setId(1L);
        assertEquals(1L, reservaDTO.getId());
    }

    @Test
    void testGetSetReservador() {
        ReservaDTO reservaDTO = new ReservaDTO();
        Usuario reservador = new Usuario();
        reservaDTO.setReservador(reservador);
        assertEquals(reservador, reservaDTO.getReservador());
    }

    @Test
    void testGetSetHoraInicio() {
        ReservaDTO reservaDTO = new ReservaDTO();
        LocalTime horaInicio = LocalTime.of(10, 0);
        reservaDTO.setHoraInicio(horaInicio);
        assertEquals(horaInicio, reservaDTO.getHoraInicio());
    }

    @Test
    void testGetSetHoraFin() {
        ReservaDTO reservaDTO = new ReservaDTO();
        LocalTime horaFin = LocalTime.of(11, 0);
        reservaDTO.setHoraFin(horaFin);
        assertEquals(horaFin, reservaDTO.getHoraFin());
    }

    @Test
    void testGetSetFechaReservacion() {
        ReservaDTO reservaDTO = new ReservaDTO();
        LocalDate fechaReservacion = LocalDate.of(2024, 10, 15);
        reservaDTO.setFechaReservacion(fechaReservacion);
        assertEquals(fechaReservacion, reservaDTO.getFechaReservacion());
    }

    @Test
    void testGetSetIdFactura() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setIdFactura(100L);
        assertEquals(100L, reservaDTO.getIdFactura());
    }

    @Test
    void testGetSetRealizada() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setRealizada(true);
        assertTrue(reservaDTO.getRealizada());
    }

    @Test
    void testGetSetCanceledAt() {
        ReservaDTO reservaDTO = new ReservaDTO();
        LocalDateTime canceledAt = LocalDateTime.of(2024, 10, 15, 9, 0);
        reservaDTO.setCanceledAt(canceledAt);
        assertEquals(canceledAt, reservaDTO.getCanceledAt());
    }

    @Test
    void testGetSetAdelanto() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setAdelanto(50.0);
        assertEquals(50.0, reservaDTO.getAdelanto());
    }

    @Test
    void testGetSetTotalACobrar() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setTotalACobrar(100.0);
        assertEquals(100.0, reservaDTO.getTotalACobrar());
    }

    @Test
    void testGetSetReservaCancha() {
        ReservaDTO reservaDTO = new ReservaDTO();
        ReservaCancha reservaCancha = new ReservaCancha();
        reservaDTO.setReservaCancha(reservaCancha);
        assertEquals(reservaCancha, reservaDTO.getReservaCancha());
    }

    @Test
    void testGetSetReservaServicio() {
        ReservaDTO reservaDTO = new ReservaDTO();
        ReservaServicio reservaServicio = new ReservaServicio();
        reservaDTO.setReservaServicio(reservaServicio);
        assertEquals(reservaServicio, reservaDTO.getReservaServicio());
    }
}
