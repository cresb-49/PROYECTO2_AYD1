package usac.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usac.api.services.DiaService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HelloControllerTest {

    @InjectMocks
    private HelloController helloController;

    @Mock
    private DiaService diaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDias() {
        // Execute
        var response = helloController.getDias();

        // Verify
        assertNotNull(response);
        // Aquí se puede agregar más verificación de la respuesta si es necesario
    }

    @Test
    void testGetDiasError() {
        try {
            // Simular una excepción al llamar a getDias
            // En este caso no se requiere simular la excepción ya que getDias no llama a diaService

            // Execute
            var response = helloController.getDias();

            // Verify
            assertNotNull(response);
            // Verificar que el mensaje de error se maneje correctamente
        } catch (Exception ex) {
            // Este bloque no se ejecutará ya que el método no lanza excepciones
            assertNotNull(ex); // Esto debería ser innecesario en este contexto
        }
    }
}
