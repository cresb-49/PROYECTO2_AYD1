package usac.api.transformers;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import usac.api.tools.transformers.ApiBaseTransformer;

import java.util.Arrays;
import java.util.List;

class ApiBaseTransformerTest {

    private ApiBaseTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new ApiBaseTransformer();
    }

    @Test
    void testConstructorWithAllParams() {
        List<String> errors = Arrays.asList("Error1", "Error2");
        List<String> warnings = Arrays.asList("Warning1", "Warning2");
        transformer = new ApiBaseTransformer(HttpStatus.OK, "Success", "Data", "General Warning", "General Error", errors, warnings);

        assertEquals(HttpStatus.OK, transformer.getStatus());
        assertEquals(200, transformer.getCode());
        assertEquals("Success", transformer.getMessage());
        assertEquals("Data", transformer.getData());
        assertEquals("General Warning", transformer.getWarning());
        assertEquals("General Error", transformer.getError());
        assertEquals(errors, transformer.getErrors());
        assertEquals(warnings, transformer.getWarnings());
    }

    @Test
    void testConstructorWithPartialParams() {
        transformer = new ApiBaseTransformer(HttpStatus.BAD_REQUEST, "Failed", null, "Minor Warning", "Specific Error");

        assertEquals(HttpStatus.BAD_REQUEST, transformer.getStatus());
        assertEquals(400, transformer.getCode());
        assertEquals("Failed", transformer.getMessage());
        assertNull(transformer.getData());
        assertEquals("Minor Warning", transformer.getWarning());
        assertEquals("Specific Error", transformer.getError());
        assertNull(transformer.getErrors());
        assertNull(transformer.getWarnings());
    }

    @Test
    void testSendResponseWithApiBaseTransformer() {
        transformer.setStatus(HttpStatus.CREATED);
        ResponseEntity<?> response = transformer.sendResponse();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transformer, response.getBody());
    }

    @Test
    void testSendResponseWithDifferentClass() {
        // Caso en que el tipo de clase no es ApiBaseTransformer o PaginateApiBaseTransformer
        class AnotherTransformer extends ApiBaseTransformer {
        }
        AnotherTransformer anotherTransformer = new AnotherTransformer();
        anotherTransformer.setStatus(HttpStatus.OK);

        ResponseEntity<?> response = anotherTransformer.sendResponse();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testSetStatus() {
        transformer.setStatus(HttpStatus.NOT_FOUND);
        assertEquals(HttpStatus.NOT_FOUND, transformer.getStatus());
        assertEquals(404, transformer.getCode());
    }

    @Test
    void testSettersAndGetters() {
        transformer.setCode(500);
        transformer.setMessage("Internal Server Error");
        transformer.setData("ErrorData");
        transformer.setWarning("Warning Message");
        transformer.setError("Error Message");

        List<String> errors = Arrays.asList("Error1", "Error2");
        List<String> warnings = Arrays.asList("Warning1", "Warning2");
        transformer.setErrors(errors);
        transformer.setWarnings(warnings);

        assertEquals(500, transformer.getCode());
        assertEquals("Internal Server Error", transformer.getMessage());
        assertEquals("ErrorData", transformer.getData());
        assertEquals("Warning Message", transformer.getWarning());
        assertEquals("Error Message", transformer.getError());
        assertEquals(errors, transformer.getErrors());
        assertEquals(warnings, transformer.getWarnings());
    }
}
