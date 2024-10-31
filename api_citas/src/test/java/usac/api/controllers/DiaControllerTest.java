package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.models.Dia;
import usac.api.services.DiaService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class DiaControllerTest {

    @InjectMocks
    private DiaController diaController;

    @Mock
    private DiaService diaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDias() {
        // Setup
        List<Dia> dias = new ArrayList<>();
        dias.add(new Dia()); // Puedes agregar más datos simulados según sea necesario
        when(diaService.getDias()).thenReturn(dias);

        // Execute
        var response = diaController.getDias();

        // Verify
        assertNotNull(response);
        verify(diaService, times(1)).getDias();
        // Aquí puedes verificar el contenido del ApiBaseTransformer si es necesario
    }

    @Test
    void testGetDiasError() {
        // Simular una excepción al llamar a getDias
        when(diaService.getDias()).thenThrow(new RuntimeException("Error en el servicio"));

        // Execute
        var response = diaController.getDias();

        // Verify
        assertNotNull(response);
        verify(diaService, times(1)).getDias(); // Verifica que se llame al método
        // También puedes verificar el estado HTTP y el mensaje de error si es necesario
    }
}
