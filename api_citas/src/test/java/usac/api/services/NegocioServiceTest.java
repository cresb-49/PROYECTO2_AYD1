package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;
import usac.api.repositories.NegocioRepository;

import javax.validation.Validator;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import usac.api.models.Dia;

class NegocioServiceTest {

    @InjectMocks
    private NegocioService negocioService;

    @Mock
    private NegocioRepository negocioRepository;

    @Mock
    private DiaService diaService;

    @Mock
    private HorarioNegocioService horarioNegocioService;

    @Mock
    private Validator validator;

    private Negocio negocioMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear un negocio de prueba
        negocioMock = new Negocio();
        negocioMock.setId(1L);
        negocioMock.setNombre("Test Negocio");
        negocioMock.setLogo("logo.png");

        // Mockear validaciones para que no interfieran con los tests
        when(validator.validate(any(Negocio.class))).thenReturn(new HashSet<>());
    }

    /**
     * Prueba para verificar que obtenerNegocio devuelve el primer negocio.
     */
    @Test
    void testObtenerNegocio() {
        when(negocioRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(negocioMock));

        // Ejecutar el método
        Negocio negocio = negocioService.obtenerNegocio();
        assertNotNull(negocio);
        assertEquals(1L, negocio.getId());
    }

    /**
     * Prueba para verificar que actualizarNegocio lanza una excepción si el
     * negocio no tiene ID.
     */
    @Test
    void testActualizarNegocioSinId() {
        Negocio negocioSinId = new Negocio();

        Exception exception = assertThrows(Exception.class, () -> negocioService.actualizarNegocio(negocioSinId, new ArrayList<>()));
        assertEquals("Id invalido", exception.getMessage());
    }

    /**
     * Prueba para verificar que actualizarNegocio lanza una excepción si el
     * negocio no existe.
     */
    @Test
    void testActualizarNegocioNoExistente() {
        when(negocioRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> negocioService.actualizarNegocio(negocioMock, new ArrayList<>()));
        assertEquals("No se encontró el negocio", exception.getMessage());
    }

    /**
     * Prueba para verificar que actualizarNegocio actualiza los datos
     * correctamente.
     *//*
    @Test
    void testActualizarNegocio() throws Exception {
        // Crear una lista de horarios de prueba
        HorarioNegocio horarioMock = new HorarioNegocio();
        horarioMock.setApertura(LocalTime.of(8, 0));
        horarioMock.setCierre(LocalTime.of(18, 0));
        Dia diaMock = new Dia();
        diaMock.setNombre("Lunes");
        horarioMock.setDia(diaMock);

        List<HorarioNegocio> nuevosHorarios = List.of(horarioMock);

        // Mockear el servicio de diaService y horarioNegocioService
        when(diaService.getDiaByNombre("Lunes")).thenReturn(diaMock);
        when(horarioNegocioService.obtenerHorarioPorDiaYNegocio(diaMock, negocioMock)).thenReturn(null);

        // Simular que el negocio ya existe con horarios nulos
        when(negocioRepository.findById(1L)).thenReturn(Optional.of(negocioMock));
        negocioMock.setHorarios(null); // Simular que los horarios son nulos

        // Ejecutar el método de actualización
        Negocio negocioActualizado = negocioService.actualizarNegocio(negocioMock, nuevosHorarios);

        // Verificar que el negocio fue actualizado correctamente
        assertNotNull(negocioActualizado);
        assertEquals("Test Negocio", negocioActualizado.getNombre());
        assertEquals(1, negocioActualizado.getHorarios().size());
        assertEquals("Lunes", negocioActualizado.getHorarios().get(0).getDia().getNombre());
        verify(negocioRepository, times(1)).save(negocioMock);
    }
*/
    /**
     * Prueba para verificar que CrearNegocio lanza una excepción si ya existe
     * un negocio registrado.
     */
    @Test
    void testCrearNegocioYaExiste() {
        when(negocioRepository.count()).thenReturn(1L);

        Exception exception = assertThrows(Exception.class, () -> negocioService.CrearNegocio(negocioMock, new ArrayList<>()));
        assertEquals("Ya existe un negocio registrado", exception.getMessage());
    }

    /**
     * Prueba para verificar que CrearNegocio crea un negocio correctamente.
     */
    @Test
    void testCrearNegocio() throws Exception {
        when(negocioRepository.count()).thenReturn(0L);

        // Mockear el resultado de guardar el negocio
        when(negocioRepository.save(negocioMock)).thenReturn(negocioMock);

        // Ejecutar el método de creación
        Negocio negocioCreado = negocioService.CrearNegocio(negocioMock, new ArrayList<>());

        // Verificar que el negocio fue creado correctamente
        assertNotNull(negocioCreado);
        assertEquals(1L, negocioCreado.getId());
        verify(negocioRepository, times(1)).save(negocioMock);
    }
}
