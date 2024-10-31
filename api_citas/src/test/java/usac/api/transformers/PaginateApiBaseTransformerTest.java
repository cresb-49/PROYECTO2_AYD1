
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import usac.api.tools.transformers.PaginateApiBaseTransformer;

import java.util.Arrays;
import java.util.List;

class PaginateApiBaseTransformerTest {

    private PaginateApiBaseTransformer transformer;

    @BeforeEach
    void setUp() {
        transformer = new PaginateApiBaseTransformer();
    }

    @Test
    void testConstructorWithAllParamsIncludingPagination() {
        List<String> errors = Arrays.asList("Error1", "Error2");
        List<String> warnings = Arrays.asList("Warning1", "Warning2");

        transformer = new PaginateApiBaseTransformer(HttpStatus.OK, "Success", "Data", "General Warning", "General Error",
                errors, warnings, 100, 10, 1, 10, "/nextPage", "/prevPage");

        assertEquals(HttpStatus.OK, transformer.getStatus());
        assertEquals(200, transformer.getCode());
        assertEquals("Success", transformer.getMessage());
        assertEquals("Data", transformer.getData());
        assertEquals("General Warning", transformer.getWarning());
        assertEquals("General Error", transformer.getError());
        assertEquals(errors, transformer.getErrors());
        assertEquals(warnings, transformer.getWarnings());
        assertEquals(100, transformer.getTotal());
        assertEquals(10, transformer.getLastPage());
        assertEquals(1, transformer.getCurrentPage());
        assertEquals(10, transformer.getPerPage());
        assertEquals("/nextPage", transformer.getNextPageUrl());
        assertEquals("/prevPage", transformer.getPrevPageUrl());
    }

    @Test
    void testConstructorWithPaginationParams() {
        transformer = new PaginateApiBaseTransformer(HttpStatus.BAD_REQUEST, "Failed", null, "Minor Warning", "Specific Error",
                50, 5, 2, 10, "/nextPage", "/prevPage");

        assertEquals(HttpStatus.BAD_REQUEST, transformer.getStatus());
        assertEquals(400, transformer.getCode());
        assertEquals("Failed", transformer.getMessage());
        assertNull(transformer.getData());
        assertEquals("Minor Warning", transformer.getWarning());
        assertEquals("Specific Error", transformer.getError());
        assertEquals(50, transformer.getTotal());
        assertEquals(5, transformer.getLastPage());
        assertEquals(2, transformer.getCurrentPage());
        assertEquals(10, transformer.getPerPage());
        assertEquals("/nextPage", transformer.getNextPageUrl());
        assertEquals("/prevPage", transformer.getPrevPageUrl());
    }

    @Test
    void testConstructorWithPartialParams() {
        transformer = new PaginateApiBaseTransformer(HttpStatus.NO_CONTENT, "No Data", null, null, null);

        assertEquals(HttpStatus.NO_CONTENT, transformer.getStatus());
        assertEquals(204, transformer.getCode());
        assertEquals("No Data", transformer.getMessage());
        assertNull(transformer.getData());
        assertNull(transformer.getWarning());
        assertNull(transformer.getError());
        assertEquals(0, transformer.getTotal());
        assertEquals(0, transformer.getLastPage());
        assertEquals(0, transformer.getCurrentPage());
        assertEquals(0, transformer.getPerPage());
        assertNull(transformer.getNextPageUrl());
        assertNull(transformer.getPrevPageUrl());
    }

    @Test
    void testSettersAndGettersForPaginationAttributes() {
        transformer.setTotal(200);
        transformer.setLastPage(20);
        transformer.setCurrentPage(2);
        transformer.setPerPage(10);
        transformer.setNextPageUrl("/nextPageUrl");
        transformer.setPrevPageUrl("/prevPageUrl");

        assertEquals(200, transformer.getTotal());
        assertEquals(20, transformer.getLastPage());
        assertEquals(2, transformer.getCurrentPage());
        assertEquals(10, transformer.getPerPage());
        assertEquals("/nextPageUrl", transformer.getNextPageUrl());
        assertEquals("/prevPageUrl", transformer.getPrevPageUrl());
    }

    @Test
    void testInheritanceFromApiBaseTransformer() {
        transformer.setStatus(HttpStatus.ACCEPTED);
        transformer.setMessage("Accepted Message");
        transformer.setData("Paginated Data");
        transformer.setWarning("Warning message");
        transformer.setError("Error message");

        assertEquals(HttpStatus.ACCEPTED, transformer.getStatus());
        assertEquals(202, transformer.getCode());
        assertEquals("Accepted Message", transformer.getMessage());
        assertEquals("Paginated Data", transformer.getData());
        assertEquals("Warning message", transformer.getWarning());
        assertEquals("Error message", transformer.getError());
    }
}
