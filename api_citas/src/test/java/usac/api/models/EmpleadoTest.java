package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoTest {

    private Empleado empleado;
    private Usuario usuario;
    private List<HorarioEmpleado> horarios;
    private List<ReservaServicio> citas;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("test@example.com");

        horarios = new ArrayList<>();
        horarios.add(new HorarioEmpleado());

        citas = new ArrayList<>();
        citas.add(new ReservaServicio());

        empleado = new Empleado(usuario);
        empleado.setHorarios(horarios);
        empleado.setCitas(citas);
    }

    @Test
    void testConstructor() {
        assertNotNull(empleado);
        assertEquals(usuario, empleado.getUsuario());
    }

    @Test
    void testSettersAndGetters() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setEmail("new@example.com");

        HorarioEmpleado nuevoHorario = new HorarioEmpleado();
        List<HorarioEmpleado> nuevosHorarios = new ArrayList<>();
        nuevosHorarios.add(nuevoHorario);

        ReservaServicio nuevaCita = new ReservaServicio();
        List<ReservaServicio> nuevasCitas = new ArrayList<>();
        nuevasCitas.add(nuevaCita);

        empleado.setUsuario(nuevoUsuario);
        empleado.setHorarios(nuevosHorarios);
        empleado.setCitas(nuevasCitas);

        assertEquals(nuevoUsuario, empleado.getUsuario());
        assertEquals(nuevosHorarios, empleado.getHorarios());
        assertEquals(nuevasCitas, empleado.getCitas());
    }

    @Test
    void testHorariosNotNull() {
        assertNotNull(empleado.getHorarios());
        assertFalse(empleado.getHorarios().isEmpty());
        assertEquals(horarios, empleado.getHorarios());
    }

    @Test
    void testCitasNotNull() {
        assertNotNull(empleado.getCitas());
        assertFalse(empleado.getCitas().isEmpty());
        assertEquals(citas, empleado.getCitas());
    }

    @Test
    void testAddHorarioEmpleado() {
        HorarioEmpleado nuevoHorario = new HorarioEmpleado();
        empleado.getHorarios().add(nuevoHorario);

        assertTrue(empleado.getHorarios().contains(nuevoHorario));
        assertEquals(2, empleado.getHorarios().size());
    }

    @Test
    void testAddReservaServicio() {
        ReservaServicio nuevaCita = new ReservaServicio();
        empleado.getCitas().add(nuevaCita);

        assertTrue(empleado.getCitas().contains(nuevaCita));
        assertEquals(2, empleado.getCitas().size());
    }
}
