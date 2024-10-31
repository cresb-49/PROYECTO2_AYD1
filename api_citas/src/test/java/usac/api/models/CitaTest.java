package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class CitaTest {

    private Usuario usuario;
    private Empleado empleado;
    private Servicio servicio;
    private Factura factura;
    private Date fecha;
    private LocalTime inicioCita;
    private LocalTime finCita;
    private Cita cita;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        empleado = new Empleado();
        servicio = new Servicio();
        factura = new Factura();
        fecha = new Date();
        inicioCita = LocalTime.of(9, 0);
        finCita = LocalTime.of(10, 0);

        cita = new Cita(usuario, empleado, servicio, false, fecha, inicioCita, finCita, factura);
    }

    @Test
    void testConstructor() {
        assertNotNull(cita);
        assertEquals(usuario, cita.getUsuario());
        assertEquals(empleado, cita.getEmpleado());
        assertEquals(servicio, cita.getServicio());
        assertFalse(cita.isRealizada());
        assertEquals(fecha, cita.getFecha());
        assertEquals(inicioCita, cita.getInicioCita());
        assertEquals(finCita, cita.getFinCita());
        assertEquals(factura, cita.getFactura());
    }

    @Test
    void testSettersAndGetters() {
        Usuario newUsuario = new Usuario();
        Empleado newEmpleado = new Empleado();
        Servicio newServicio = new Servicio();
        Factura newFactura = new Factura();
        Date newFecha = new Date();
        LocalTime newInicioCita = LocalTime.of(11, 0);
        LocalTime newFinCita = LocalTime.of(12, 0);

        cita.setUsuario(newUsuario);
        cita.setEmpleado(newEmpleado);
        cita.setServicio(newServicio);
        cita.setRealizada(true);
        cita.setFecha(newFecha);
        cita.setInicioCita(newInicioCita);
        cita.setFinCita(newFinCita);
        cita.setFactura(newFactura);

        assertEquals(newUsuario, cita.getUsuario());
        assertEquals(newEmpleado, cita.getEmpleado());
        assertEquals(newServicio, cita.getServicio());
        assertTrue(cita.isRealizada());
        assertEquals(newFecha, cita.getFecha());
        assertEquals(newInicioCita, cita.getInicioCita());
        assertEquals(newFinCita, cita.getFinCita());
        assertEquals(newFactura, cita.getFactura());
    }

    @Test
    void testFechaNotNull() {
        assertNotNull(cita.getFecha(), "La fecha no debe ser nula");
    }

    @Test
    void testInicioCitaNotNull() {
        assertNotNull(cita.getInicioCita(), "La hora de inicio no debe ser nula");
    }

    @Test
    void testFinCitaNotNull() {
        assertNotNull(cita.getFinCita(), "La hora de fin no debe ser nula");
    }

    @Test
    void testDefaultRealizada() {
        Cita citaNueva = new Cita(usuario, empleado, servicio, false, fecha, inicioCita, finCita, null);
        assertFalse(citaNueva.isRealizada(), "La cita debe tener el valor predeterminado de 'realizada' en falso");
    }
}
