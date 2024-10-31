package usac.api.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class HorarioEmpleadoTest {

    private HorarioEmpleado horarioEmpleado;
    private Dia dia;
    private Empleado empleado;
    private LocalTime entrada;
    private LocalTime salida;

    @BeforeEach
    void setUp() {
        dia = new Dia("Lunes");
        empleado = new Empleado(new Usuario());
        entrada = LocalTime.of(9, 0);
        salida = LocalTime.of(17, 0);

        horarioEmpleado = new HorarioEmpleado(dia, empleado, entrada, salida);
    }

    @Test
    void testConstructor() {
        assertNotNull(horarioEmpleado);
        assertEquals(dia, horarioEmpleado.getDia());
        assertEquals(empleado, horarioEmpleado.getEmpleado());
        assertEquals(entrada, horarioEmpleado.getEntrada());
        assertEquals(salida, horarioEmpleado.getSalida());
    }

    @Test
    void testSettersAndGetters() {
        Dia nuevoDia = new Dia("Martes");
        Empleado nuevoEmpleado = new Empleado(new Usuario());
        LocalTime nuevaEntrada = LocalTime.of(8, 0);
        LocalTime nuevaSalida = LocalTime.of(16, 0);

        horarioEmpleado.setDia(nuevoDia);
        horarioEmpleado.setEmpleado(nuevoEmpleado);
        horarioEmpleado.setEntrada(nuevaEntrada);
        horarioEmpleado.setSalida(nuevaSalida);

        assertEquals(nuevoDia, horarioEmpleado.getDia());
        assertEquals(nuevoEmpleado, horarioEmpleado.getEmpleado());
        assertEquals(nuevaEntrada, horarioEmpleado.getEntrada());
        assertEquals(nuevaSalida, horarioEmpleado.getSalida());
    }

    @Test
    void testEntradaNotNull() {
        assertNotNull(horarioEmpleado.getEntrada());
        assertEquals(entrada, horarioEmpleado.getEntrada());
    }

    @Test
    void testSalidaNotNull() {
        assertNotNull(horarioEmpleado.getSalida());
        assertEquals(salida, horarioEmpleado.getSalida());
    }

    @Test
    void testChangeHorario() {
        LocalTime nuevaEntrada = LocalTime.of(10, 0);
        LocalTime nuevaSalida = LocalTime.of(18, 0);

        horarioEmpleado.setEntrada(nuevaEntrada);
        horarioEmpleado.setSalida(nuevaSalida);

        assertEquals(nuevaEntrada, horarioEmpleado.getEntrada());
        assertEquals(nuevaSalida, horarioEmpleado.getSalida());
    }
}
