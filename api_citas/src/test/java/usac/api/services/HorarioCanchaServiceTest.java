package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Cancha;
import usac.api.models.Dia;
import usac.api.models.HorarioCancha;
import usac.api.repositories.HorarioCanchaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HorarioCanchaServiceTest {

    @InjectMocks
    private HorarioCanchaService horarioCanchaService;

    @Mock
    private HorarioCanchaRepository horarioCanchaRepository;

    private Cancha cancha;
    private Dia dia;
    private HorarioCancha horarioCancha;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cancha = new Cancha();
        cancha.setId(1L);

        dia = new Dia();
        dia.setNombre("Lunes");

        horarioCancha = new HorarioCancha();
        horarioCancha.setCancha(cancha);
        horarioCancha.setDia(dia);
    }

    @Test
    void testObtenerTodosLosHorariosIncluyendoEliminados() {
        List<HorarioCancha> horarios = List.of(horarioCancha);

        when(horarioCanchaRepository.findAllByCanchaIncludingDeleted(cancha)).thenReturn(horarios);

        List<HorarioCancha> result = horarioCanchaService.obtenerTodosLosHorariosIncluyendoEliminados(cancha);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(horarios.size(), result.size());
        verify(horarioCanchaRepository, times(1)).findAllByCanchaIncludingDeleted(cancha);
    }

    @Test
    void testObtenerHorarioPorDiaYCancha() {
        when(horarioCanchaRepository.findByDiaAndCancha(dia, cancha)).thenReturn(horarioCancha);

        HorarioCancha result = horarioCanchaService.obtenerHorarioPorDiaYCancha(dia, cancha);

        assertNotNull(result);
        assertEquals(horarioCancha, result);
        verify(horarioCanchaRepository, times(1)).findByDiaAndCancha(dia, cancha);
    }
}
