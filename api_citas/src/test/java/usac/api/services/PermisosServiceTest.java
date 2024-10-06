package usac.api.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Permiso;
import usac.api.repositories.PermisoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermisoServiceTest {

    @InjectMocks
    private PermisoService permisoService;

    @Mock
    private PermisoRepository permisoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para verificar que el método getPermisoById devuelve el permiso
     * correctamente cuando el permiso existe en la base de datos.
     */
    @Test
    void testGetPermisoByIdPermisoEncontrado() throws Exception {
        // Crear un permiso de prueba
        Permiso permisoMock = new Permiso();
        permisoMock.setId(1L);

        // Simular la respuesta del repositorio
        when(permisoRepository.findById(1L)).thenReturn(Optional.of(permisoMock));

        // Ejecutar el método y verificar
        Permiso permisoObtenido = permisoService.getPermisoById(permisoMock);
        assertNotNull(permisoObtenido);
        assertEquals(1L, permisoObtenido.getId(), "El id del permiso obtenido debe ser 1.");
    }

    /**
     * Prueba para verificar que el método getPermisoById lanza una excepción
     * cuando el permiso no existe.
     */
    @Test
    void testGetPermisoByIdPermisoNoEncontrado() throws Exception {
        // Crear un permiso de prueba con id no encontrado
        Permiso permisoMock = new Permiso();
        permisoMock.setId(1L);

        // Simular la respuesta del repositorio con un Optional vacío
        when(permisoRepository.findById(1L)).thenReturn(Optional.empty());

        // Verificar que se lanza una excepción al no encontrar el permiso
        Exception exception = assertThrows(Exception.class, () -> permisoService.getPermisoById(permisoMock));
        assertEquals("Informacion no encontrada.", exception.getMessage());
    }

    /**
     * Prueba para verificar que se lanza una excepción al pasar un permiso
     * nulo.
     */
    @Test
    void testGetPermisoByIdConPermisoNulo() {
        Permiso permisoNulo = null;

        // Verificar que se lanza una excepción al intentar validar un permiso nulo
        Exception exception = assertThrows(Exception.class, () -> permisoService.getPermisoById(permisoNulo));
        assertEquals("Id invalido", exception.getMessage());
    }
}
