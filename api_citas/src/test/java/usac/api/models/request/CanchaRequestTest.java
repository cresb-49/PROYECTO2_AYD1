package usac.api.models.request;

import org.junit.jupiter.api.Test;
import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CanchaRequestTest {

    /**
     * Prueba para verificar la correcta creación del objeto CanchaRequest con
     * el constructor vacío.
     */
    @Test
    void testCanchaRequestConstructorVacio() {
        CanchaRequest request = new CanchaRequest();
        assertNull(request.getCancha());
        assertNull(request.getHorarios());
    }

    /**
     * Prueba para verificar la creación del objeto CanchaRequest con el
     * constructor que acepta parámetros.
     */
    @Test
    void testCanchaRequestConstructorConParametros() {
        Cancha cancha = new Cancha();
        HorarioCancha horario1 = new HorarioCancha();
        HorarioCancha horario2 = new HorarioCancha();
        List<HorarioCancha> horarios = List.of(horario1, horario2);

        CanchaRequest request = new CanchaRequest(cancha, horarios);

        assertEquals(cancha, request.getCancha());
        assertEquals(horarios, request.getHorarios());
    }

    /**
     * Prueba para verificar el funcionamiento del método setCancha.
     */
    @Test
    void testSetCancha() {
        CanchaRequest request = new CanchaRequest();
        Cancha cancha = new Cancha();
        request.setCancha(cancha);

        assertEquals(cancha, request.getCancha());
    }

    /**
     * Prueba para verificar el funcionamiento del método setHorarios.
     */
    @Test
    void testSetHorarios() {
        CanchaRequest request = new CanchaRequest();
        HorarioCancha horario1 = new HorarioCancha();
        HorarioCancha horario2 = new HorarioCancha();
        List<HorarioCancha> horarios = List.of(horario1, horario2);

        request.setHorarios(horarios);

        assertEquals(horarios, request.getHorarios());
    }

    /**
     * Prueba para verificar el método toString de CanchaRequest.
     */
    @Test
    void testToString() {
        Cancha cancha = new Cancha();
        HorarioCancha horario1 = new HorarioCancha();
        HorarioCancha horario2 = new HorarioCancha();
        List<HorarioCancha> horarios = List.of(horario1, horario2);

        CanchaRequest request = new CanchaRequest(cancha, horarios);
        String expectedString = "CanchaRequest [cancha=" + cancha + ", horarios=" + horarios + "]";

        assertEquals(expectedString, request.toString());
    }
}
