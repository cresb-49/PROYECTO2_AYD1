package usac.api.models.dto;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ArchivoDTOTest {

    /**
     * Prueba para verificar que el constructor inicializa los campos
     * correctamente.
     */
    @Test
    void testConstructor() {
        HttpHeaders headersEsperados = new HttpHeaders();
        headersEsperados.add("Content-Type", "application/pdf");
        byte[] archivoEsperado = {1, 2, 3, 4};

        ArchivoDTO dto = new ArchivoDTO(headersEsperados, archivoEsperado);

        assertEquals(headersEsperados, dto.getHeaders());
        assertArrayEquals(archivoEsperado, dto.getArchivo());
    }

    /**
     * Prueba para verificar el constructor vacío.
     */
    @Test
    void testConstructorVacio() {
        ArchivoDTO dto = new ArchivoDTO();

        assertNull(dto.getHeaders());
        assertNull(dto.getArchivo());
    }

    /**
     * Prueba para verificar que los headers se pueden obtener correctamente.
     */
    @Test
    void testGetHeaders() {
        HttpHeaders headersEsperados = new HttpHeaders();
        headersEsperados.add("Authorization", "Bearer token");

        ArchivoDTO dto = new ArchivoDTO();
        dto.setHeaders(headersEsperados);

        assertEquals(headersEsperados, dto.getHeaders());
    }

    /**
     * Prueba para verificar que los headers se pueden modificar correctamente.
     */
    @Test
    void testSetHeaders() {
        HttpHeaders headersIniciales = new HttpHeaders();
        headersIniciales.add("Initial", "Value");

        ArchivoDTO dto = new ArchivoDTO();
        dto.setHeaders(headersIniciales);

        HttpHeaders nuevosHeaders = new HttpHeaders();
        nuevosHeaders.add("Updated", "Value");
        dto.setHeaders(nuevosHeaders);

        assertEquals(nuevosHeaders, dto.getHeaders());
    }

    /**
     * Prueba para verificar que el archivo se puede obtener correctamente.
     */
    @Test
    void testGetArchivo() {
        byte[] archivoEsperado = {5, 6, 7, 8};
        ArchivoDTO dto = new ArchivoDTO();
        dto.setArchivo(archivoEsperado);

        assertArrayEquals(archivoEsperado, dto.getArchivo());
    }

    /**
     * Prueba para verificar que el archivo se puede modificar correctamente.
     */
    @Test
    void testSetArchivo() {
        byte[] archivoInicial = {9, 10, 11, 12};
        ArchivoDTO dto = new ArchivoDTO();
        dto.setArchivo(archivoInicial);

        byte[] nuevoArchivo = {13, 14, 15, 16};
        dto.setArchivo(nuevoArchivo);

        assertArrayEquals(nuevoArchivo, dto.getArchivo());
    }
}
