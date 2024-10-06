package usac.api.models.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para {@link NegocioPublicDTO}. Se verifican los
 * getters, setters y constructores.
 */
public class NegocioPublicDTOTest {

    /**
     * Verifica que el constructor vacío de {@link NegocioPublicDTO} inicializa
     * los valores como nulos.
     */
    @Test
    void testConstructorVacio() {
        NegocioPublicDTO negocio = new NegocioPublicDTO();

        assertNull(negocio.getId(), "El ID debe ser nulo.");
        assertNull(negocio.getNombre(), "El nombre debe ser nulo.");
        assertNull(negocio.getLogo(), "El logo debe ser nulo.");
    }

    /**
     * Verifica que el constructor con parámetros de {@link NegocioPublicDTO}
     * asigne correctamente los valores.
     */
    @Test
    void testConstructorConParametros() {
        NegocioPublicDTO negocio = new NegocioPublicDTO(1L, "Mi Negocio", "logo.png");

        assertEquals(1L, negocio.getId(), "El ID no es el esperado.");
        assertEquals("Mi Negocio", negocio.getNombre(), "El nombre no es el esperado.");
        assertEquals("logo.png", negocio.getLogo(), "El logo no es el esperado.");
    }

    /**
     * Verifica el correcto funcionamiento de los getters y setters.
     */
    @Test
    void testGettersYSetters() {
        NegocioPublicDTO negocio = new NegocioPublicDTO();

        // Verificar el setter y getter de ID
        negocio.setId(2L);
        assertEquals(2L, negocio.getId(), "El ID no fue establecido correctamente.");

        // Verificar el setter y getter de nombre
        negocio.setNombre("Nuevo Negocio");
        assertEquals("Nuevo Negocio", negocio.getNombre(), "El nombre no fue establecido correctamente.");

        // Verificar el setter y getter de logo
        negocio.setLogo("nuevo_logo.png");
        assertEquals("nuevo_logo.png", negocio.getLogo(), "El logo no fue establecido correctamente.");
    }
}
