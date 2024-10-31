package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Permiso;
import usac.api.services.PermisoService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PermisoControllerTest {

    @InjectMocks
    private PermisoController permisoController;

    @Mock
    private PermisoService permisoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPermisos() {
        // Setup
        List<Permiso> permisos = new ArrayList<>();
        permisos.add(new Permiso()); // Agrega un permiso simulado
        when(permisoService.getAllPermisos()).thenReturn(permisos);

        // Execute
        var response = permisoController.getPermisos();

        // Verify
        assertNotNull(response);
        verify(permisoService, times(1)).getAllPermisos();
        // Aquí puedes verificar el contenido del ApiBaseTransformer si es necesario
    }

    @Test
    void testGetPermisosError() {
        // Simular una excepción al llamar a getAllPermisos
        when(permisoService.getAllPermisos()).thenThrow(new RuntimeException("Error en el servicio"));

        // Execute
        var response = permisoController.getPermisos();

        // Verify
        assertNotNull(response);
        verify(permisoService, times(1)).getAllPermisos(); // Verifica que se llame al método
        // También puedes verificar el estado HTTP y el mensaje de error si es necesario
    }
}
