
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;
import usac.api.repositories.CanchaRepository;

import javax.validation.Validator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import usac.api.services.CanchaService;
import usac.api.services.DiaService;
import usac.api.services.HorarioCanchaService;

class CanchaServiceTest {

    @InjectMocks
    private CanchaService canchaService;

    @Mock
    private CanchaRepository canchaRepository;

    @Mock
    private HorarioCanchaService horarioCanchaService;

    @Mock
    private DiaService diaService;

    @Mock
    private Validator validator;

    private Cancha cancha;
    private List<HorarioCancha> horarios;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cancha = new Cancha();
        cancha.setId(1L);
        cancha.setDescripcion("Cancha de fútbol");
        cancha.setCostoHora(100.0);

        horarios = new ArrayList<>();
        HorarioCancha horario = new HorarioCancha();
        horario.setApertura(LocalTime.of(8, 0));
        horario.setCierre(LocalTime.of(20, 0));
        horarios.add(horario);
    }

    @Test
    void testCrearCanchaFalla() {
        assertThrows(Exception.class, () -> canchaService.crearCancha(cancha, new ArrayList<>()));
    }

    @Test
    void testActualizarCanchaInvalido() {
        Cancha canchaSinId = new Cancha();

        Exception exception = assertThrows(Exception.class, () -> canchaService.actualizarCancha(canchaSinId, horarios));
        assertEquals("Id invalido", exception.getMessage());
    }

    @Test
    void testGetCanchaByIdExitoso() throws Exception {
        when(canchaRepository.findById(anyLong())).thenReturn(Optional.of(cancha));

        Cancha canchaObtenida = canchaService.getCanchaById(1L);

        assertNotNull(canchaObtenida);
        assertEquals(cancha.getId(), canchaObtenida.getId());
        verify(canchaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCanchaByIdNoEncontrada() {
        when(canchaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> canchaService.getCanchaById(1L));
        assertEquals("No se encontró la cancha", exception.getMessage());
    }

    @Test
    void testDeleteCanchaByIdExitoso() throws Exception {
        when(canchaRepository.findById(anyLong())).thenReturn(Optional.of(cancha));
        doAnswer(invocation -> null).when(canchaRepository).deleteCanchaById(anyLong());

        canchaService.deleteCanchaById(1L);

        verify(canchaRepository, times(1)).deleteCanchaById(1L);
    }

    @Test
    void testDeleteCanchaByIdNoEncontrada() {
        when(canchaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> canchaService.deleteCanchaById(1L));
        assertEquals("No se encontró la cancha", exception.getMessage());
    }

    @Test
    void testCountCanchas() {
        when(canchaRepository.count()).thenReturn(5L);

        int count = canchaService.countCanchas();

        assertEquals(5, count);
        verify(canchaRepository, times(1)).count();
    }

    @Test
    void testGetCanchas() {
        List<Cancha> canchas = List.of(cancha);
        when(canchaRepository.findAll()).thenReturn(canchas);

        List<Cancha> result = canchaService.getCanchas();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(canchas.size(), result.size());
        verify(canchaRepository, times(1)).findAll();
    }
}
