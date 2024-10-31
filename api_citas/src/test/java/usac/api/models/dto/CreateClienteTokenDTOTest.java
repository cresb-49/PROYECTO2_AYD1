package usac.api.models.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateClienteTokenDTOTest {

    /**
     * Prueba para verificar que el token se establece correctamente en el
     * constructor.
     */
    @Test
    void testConstructor() {
        String tokenEsperado = "abc123";
        CreateClienteTokenDTO dto = new CreateClienteTokenDTO(tokenEsperado);

        assertEquals(tokenEsperado, dto.getToken());
    }

    /**
     * Prueba para verificar que el token se puede obtener correctamente.
     */
    @Test
    void testGetToken() {
        String tokenEsperado = "def456";
        CreateClienteTokenDTO dto = new CreateClienteTokenDTO(tokenEsperado);

        assertEquals(tokenEsperado, dto.getToken());
    }

    /**
     * Prueba para verificar que el token se puede modificar correctamente.
     */
    @Test
    void testSetToken() {
        String tokenInicial = "ghi789";
        CreateClienteTokenDTO dto = new CreateClienteTokenDTO(tokenInicial);

        String nuevoToken = "jkl012";
        dto.setToken(nuevoToken);

        assertEquals(nuevoToken, dto.getToken());
    }
}
