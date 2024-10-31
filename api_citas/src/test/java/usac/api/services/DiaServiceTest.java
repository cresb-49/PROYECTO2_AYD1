package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Dia;
import usac.api.repositories.DiaRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiaServiceTest {

    @InjectMocks
    private DiaService diaService;

    @Mock
    private DiaRepository diaRepository;

    private Dia dia;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dia = new Dia();
        dia.setId(1L);
        dia.setNombre("Lunes");
    }

    @Test
    void testGetDiaByNombreExitoso() throws Exception {
        when(diaRepository.findOneByNombreIgnoreCase(anyString())).thenReturn(Optional.of(dia));

        Dia diaObtenido = diaService.getDiaByNombre("Lunes");

        assertNotNull(diaObtenido);
        assertEquals(dia.getNombre(), diaObtenido.getNombre());
        verify(diaRepository, times(1)).findOneByNombreIgnoreCase("Lunes");
    }

    @Test
    void testGetDiaByNombreNoEncontrado() {
        when(diaRepository.findOneByNombreIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> diaService.getDiaByNombre("Domingo"));
        assertEquals("Dia no encontrado.", exception.getMessage());
    }

    @Test
    void testGetDiaByIdExitoso() throws Exception {
        when(diaRepository.findById(anyLong())).thenReturn(Optional.of(dia));

        Dia diaObtenido = diaService.getDiaById(1L);

        assertNotNull(diaObtenido);
        assertEquals(dia.getId(), diaObtenido.getId());
        verify(diaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDiaByIdNoEncontrado() {
        when(diaRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> diaService.getDiaById(1L));
        assertEquals("Dia no encontrado.", exception.getMessage());
    }

    @Test
    void testGetDias() {
        List<Dia> dias = List.of(dia);
        when(diaRepository.findAll()).thenReturn(dias);

        List<Dia> resultado = diaService.getDias();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(dias.size(), resultado.size());
        verify(diaRepository, times(1)).findAll();
    }

}
