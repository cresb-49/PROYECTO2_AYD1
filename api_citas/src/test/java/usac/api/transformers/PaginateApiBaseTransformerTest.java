/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.transformers;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import usac.api.tools.transformers.PaginateApiBaseTransformer;

/**
 *
 * @author Luis Monterroso
 */
public class PaginateApiBaseTransformerTest {

    private PaginateApiBaseTransformer paginateTransformer;

    @BeforeEach
    void setUp() {
        // Inicializar el objeto antes de cada prueba
        paginateTransformer = new PaginateApiBaseTransformer();
    }

    /**
     * Prueba del constructor completo de PaginateApiBaseTransformer.
     */
    @Test
    void testConstructorComplete() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        List<String> warnings = Arrays.asList("Warning 1", "Warning 2");

        paginateTransformer = new PaginateApiBaseTransformer(
                HttpStatus.OK, "Success", "Data", "Warning", "Error", errors, warnings,
                100, 10, 1, 10, "nextPageUrl", "prevPageUrl");

        assertEquals(HttpStatus.OK, paginateTransformer.getStatus());
        assertEquals("Success", paginateTransformer.getMessage());
        assertEquals("Data", paginateTransformer.getData());
        assertEquals("Warning", paginateTransformer.getWarning());
        assertEquals("Error", paginateTransformer.getError());
        assertEquals(errors, paginateTransformer.getErrors());
        assertEquals(warnings, paginateTransformer.getWarnings());
        assertEquals(100, paginateTransformer.getTotal());
        assertEquals(10, paginateTransformer.getLastPage());
        assertEquals(1, paginateTransformer.getCurrentPage());
        assertEquals(10, paginateTransformer.getPerPage());
        assertEquals("nextPageUrl", paginateTransformer.getNextPageUrl());
        assertEquals("prevPageUrl", paginateTransformer.getPrevPageUrl());
    }

    /**
     * Prueba del constructor que no incluye errores ni advertencias.
     */
    @Test
    void testConstructorWithoutErrorsAndWarnings() {
        paginateTransformer = new PaginateApiBaseTransformer(
                HttpStatus.OK, "Success", "Data", "Warning", "Error",
                100, 10, 1, 10, "nextPageUrl", "prevPageUrl");

        assertEquals(HttpStatus.OK, paginateTransformer.getStatus());
        assertEquals("Success", paginateTransformer.getMessage());
        assertEquals("Data", paginateTransformer.getData());
        assertEquals("Warning", paginateTransformer.getWarning());
        assertEquals("Error", paginateTransformer.getError());
        assertNull(paginateTransformer.getErrors());
        assertNull(paginateTransformer.getWarnings());
        assertEquals(100, paginateTransformer.getTotal());
        assertEquals(10, paginateTransformer.getLastPage());
        assertEquals(1, paginateTransformer.getCurrentPage());
        assertEquals(10, paginateTransformer.getPerPage());
        assertEquals("nextPageUrl", paginateTransformer.getNextPageUrl());
        assertEquals("prevPageUrl", paginateTransformer.getPrevPageUrl());
    }

    /**
     * Prueba para verificar los getters y setters de los atributos de
     * paginación.
     */
    @Test
    void testSettersAndGetters() {
        paginateTransformer.setTotal(200);
        paginateTransformer.setLastPage(20);
        paginateTransformer.setCurrentPage(2);
        paginateTransformer.setPerPage(20);
        paginateTransformer.setNextPageUrl("nextPageUrlTest");
        paginateTransformer.setPrevPageUrl("prevPageUrlTest");

        assertEquals(200, paginateTransformer.getTotal());
        assertEquals(20, paginateTransformer.getLastPage());
        assertEquals(2, paginateTransformer.getCurrentPage());
        assertEquals(20, paginateTransformer.getPerPage());
        assertEquals("nextPageUrlTest", paginateTransformer.getNextPageUrl());
        assertEquals("prevPageUrlTest", paginateTransformer.getPrevPageUrl());
    }
}
