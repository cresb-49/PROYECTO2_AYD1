package usac.api.models.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;

/**
 * Clase de pruebas unitarias para {@link NegocioUpdateDTO}. Se verifican los
 * getters, setters y constructores.
 */
public class NegocioUpdateDTOTest {

    /**
     * Verifica que el constructor vacío de {@link NegocioUpdateDTO} inicializa
     * los valores como nulos.
     */
    @Test
    void testConstructorVacio() {
        NegocioUpdateDTO negocioUpdateDTO = new NegocioUpdateDTO();

        assertNull(negocioUpdateDTO.getNegocio(), "El objeto negocio debe ser nulo.");
        assertNull(negocioUpdateDTO.getHorarios(), "La lista de horarios debe ser nula.");
    }

    /**
     * Verifica que el constructor con parámetros de {@link NegocioUpdateDTO}
     * asigne correctamente los valores.
     */
    @Test
    void testConstructorConParametros() {
        Negocio negocio = new Negocio();
        List<HorarioNegocio> horarios = new ArrayList<>();

        NegocioUpdateDTO negocioUpdateDTO = new NegocioUpdateDTO(horarios, negocio);

        assertEquals(negocio, negocioUpdateDTO.getNegocio(), "El objeto negocio no fue asignado correctamente.");
        assertEquals(horarios, negocioUpdateDTO.getHorarios(), "La lista de horarios no fue asignada correctamente.");
    }

    /**
     * Verifica el correcto funcionamiento de los getters y setters.
     */
    @Test
    void testGettersYSetters() {
        NegocioUpdateDTO negocioUpdateDTO = new NegocioUpdateDTO();

        // Crear objetos de prueba
        Negocio negocio = new Negocio();
        List<HorarioNegocio> horarios = new ArrayList<>();

        // Verificar el setter y getter de negocio
        negocioUpdateDTO.setNegocio(negocio);
        assertEquals(negocio, negocioUpdateDTO.getNegocio(), "El objeto negocio no fue establecido correctamente.");

        // Verificar el setter y getter de horarios
        negocioUpdateDTO.setHorarios(horarios);
        assertEquals(horarios, negocioUpdateDTO.getHorarios(), "La lista de horarios no fue establecida correctamente.");
    }
}
