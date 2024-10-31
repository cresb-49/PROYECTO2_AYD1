
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;
import usac.api.models.ReservaCancha;

class CanchaTest {

    private Cancha cancha;

    @BeforeEach
    void setUp() {
        cancha = new Cancha(150.0, "Cancha de Fútbol Sala");
    }

    @Test
    void testConstructorAndAttributes() {
        assertEquals(150.0, cancha.getCostoHora());
        assertEquals("Cancha de Fútbol Sala", cancha.getDescripcion());
    }

    @Test
    void testSetCostoHora() {
        cancha.setCostoHora(200.0);
        assertEquals(200.0, cancha.getCostoHora());
    }

    @Test
    void testSetDescripcion() {
        cancha.setDescripcion("Cancha de Baloncesto");
        assertEquals("Cancha de Baloncesto", cancha.getDescripcion());
    }

    @Test
    void testSetHorarios() {
        List<HorarioCancha> horarios = new ArrayList<>();
        cancha.setHorarios(horarios);
        assertEquals(horarios, cancha.getHorarios());
    }

    @Test
    void testSetReservas() {
        List<ReservaCancha> reservas = new ArrayList<>();
        cancha.setReservas(reservas);
        assertEquals(reservas, cancha.getReservas());
    }
}
