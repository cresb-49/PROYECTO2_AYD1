/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.transformers;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import usac.api.tools.transformers.ApiBaseTransformer;

/**
 *
 * @author Luis Monterroso
 */
public class ApiBaseTransformerTest {

    private ApiBaseTransformer apiBaseTransformer;

    @BeforeEach
    void setUp() {
        // Puedes instanciar la clase manualmente en cada prueba
        apiBaseTransformer = new ApiBaseTransformer(HttpStatus.OK, "Success", "Data", null, null);
    }

    /**
     * Prueba para verificar la creación del objeto ApiBaseTransformer
     * utilizando un constructor completo.
     */
    @Test
    void testConstructorComplete() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        List<String> warnings = Arrays.asList("Warning 1", "Warning 2");

        apiBaseTransformer = new ApiBaseTransformer(HttpStatus.OK, "Success", "Data", "Warning", "Error", errors, warnings);

        assertEquals(HttpStatus.OK, apiBaseTransformer.getStatus());
        assertEquals(200, apiBaseTransformer.getCode());
        assertEquals("Success", apiBaseTransformer.getMessage());
        assertEquals("Data", apiBaseTransformer.getData());
        assertEquals("Warning", apiBaseTransformer.getWarning());
        assertEquals("Error", apiBaseTransformer.getError());
        assertEquals(errors, apiBaseTransformer.getErrors());
        assertEquals(warnings, apiBaseTransformer.getWarnings());
    }

    /**
     * Prueba para verificar la creación del objeto ApiBaseTransformer
     * utilizando el segundo constructor (sin listas de errores y advertencias).
     */
    @Test
    void testConstructorWithoutErrorsAndWarnings() {
        apiBaseTransformer = new ApiBaseTransformer(HttpStatus.OK, "Success", "Data", "Warning", "Error");

        assertEquals(HttpStatus.OK, apiBaseTransformer.getStatus());
        assertEquals(200, apiBaseTransformer.getCode());
        assertEquals("Success", apiBaseTransformer.getMessage());
        assertEquals("Data", apiBaseTransformer.getData());
        assertEquals("Warning", apiBaseTransformer.getWarning());
        assertEquals("Error", apiBaseTransformer.getError());
        assertNull(apiBaseTransformer.getErrors());
        assertNull(apiBaseTransformer.getWarnings());
    }

    /**
     * Prueba para verificar el método sendResponse.
     */
    @Test
    void testSendResponse_BaseTransformer() {
        apiBaseTransformer = new ApiBaseTransformer(HttpStatus.OK, "Success", "Data", null, null);

        ResponseEntity<?> response = apiBaseTransformer.sendResponse();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ApiBaseTransformer);
    }

    /**
     * Prueba para verificar que el método sendResponse retorna un error de
     * servidor interno cuando la clase no es ApiBaseTransformer.
     */
    @Test
    void testSendResponse_InternalServerError() {
        // Creamos una instancia de ApiBaseTransformer con error interno
        apiBaseTransformer = new ApiBaseTransformer(HttpStatus.INTERNAL_SERVER_ERROR, "Error", null, null, "Error");

        // Llamamos al método sendResponse
        ResponseEntity<?> response = apiBaseTransformer.sendResponse();

        // Verificamos que el código de estado sea INTERNAL_SERVER_ERROR
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verificamos que el cuerpo de la respuesta no sea null
        assertTrue(response.getBody() instanceof ApiBaseTransformer);
    }

    /**
     * Prueba para verificar el setter y getter del código de estado.
     */
    @Test
    void testSetAndGetStatus() {
        apiBaseTransformer = new ApiBaseTransformer();
        apiBaseTransformer.setStatus(HttpStatus.CREATED);

        assertEquals(HttpStatus.CREATED, apiBaseTransformer.getStatus());
        assertEquals(201, apiBaseTransformer.getCode());
    }

    /**
     * Prueba para verificar los setters y getters de los atributos.
     */
    @Test
    void testSettersAndGetters() {
        apiBaseTransformer = new ApiBaseTransformer();
        apiBaseTransformer.setMessage("Test message");
        apiBaseTransformer.setData("Test data");
        apiBaseTransformer.setWarning("Test warning");
        apiBaseTransformer.setError("Test error");

        assertEquals("Test message", apiBaseTransformer.getMessage());
        assertEquals("Test data", apiBaseTransformer.getData());
        assertEquals("Test warning", apiBaseTransformer.getWarning());
        assertEquals("Test error", apiBaseTransformer.getError());
    }

    /**
     * Prueba para verificar el manejo de listas de errores y advertencias.
     */
    @Test
    void testSetAndGetErrorsAndWarnings() {
        apiBaseTransformer = new ApiBaseTransformer();

        List<String> errors = Arrays.asList("Error 1", "Error 2");
        List<String> warnings = Arrays.asList("Warning 1", "Warning 2");

        apiBaseTransformer.setErrors(errors);
        apiBaseTransformer.setWarnings(warnings);

        assertEquals(errors, apiBaseTransformer.getErrors());
        assertEquals(warnings, apiBaseTransformer.getWarnings());
    }
}
