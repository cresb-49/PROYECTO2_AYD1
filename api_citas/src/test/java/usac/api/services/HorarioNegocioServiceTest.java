package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Dia;
import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;
import usac.api.repositories.HorarioNegocioRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HorarioNegocioServiceTest {

    @InjectMocks
    private HorarioNegocioService horarioNegocioService;

    @Mock
    private HorarioNegocioRepository horarioNegocioRepository;

    private Negocio negocio;
    private Dia dia;
    private HorarioNegocio horarioNegocio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        negocio = new Negocio();
        negocio.setId(1L);

        dia = new Dia();
        dia.setNombre("Lunes");

        horarioNegocio = new HorarioNegocio();
        horarioNegocio.setNegocio(negocio);
        horarioNegocio.setDia(dia);
    }

    @Test
    void testObtenerTodosLosHorariosIncluyendoEliminados() {
        List<HorarioNegocio> horarios = List.of(horarioNegocio);

        when(horarioNegocioRepository.findAllByNegocioIncludingDeleted(negocio)).thenReturn(horarios);

        List<HorarioNegocio> result = horarioNegocioService.obtenerTodosLosHorariosIncluyendoEliminados(negocio);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(horarios.size(), result.size());
        verify(horarioNegocioRepository, times(1)).findAllByNegocioIncludingDeleted(negocio);
    }

    @Test
    void testObtenerHorarioPorDiaYNegocio() {
        when(horarioNegocioRepository.findByDiaAndNegocio(dia, negocio)).thenReturn(horarioNegocio);

        HorarioNegocio result = horarioNegocioService.obtenerHorarioPorDiaYNegocio(dia, negocio);

        assertNotNull(result);
        assertEquals(horarioNegocio, result);
        verify(horarioNegocioRepository, times(1)).findByDiaAndNegocio(dia, negocio);
    }
}
