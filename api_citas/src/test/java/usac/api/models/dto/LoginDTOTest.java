/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.Usuario;

/**
 *
 * @author Luis Monterroso
 */
public class LoginDTOTest {

    private LoginDTO loginDTO;
    private Usuario usuario;
    private String jwt;

    @BeforeEach
    void setUp() {
        // Crear instancias de usuario y jwt
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");

        jwt = "mocked-jwt";
    }

    @Test
    void testConstructorWithUsuarioAndJwt() {
        loginDTO = new LoginDTO(usuario, jwt);

        assertEquals(usuario, loginDTO.getUsuario());
        assertEquals(jwt, loginDTO.getJwt());
        assertFalse(loginDTO.isHasTwoFactorCode());  // Por defecto debería ser false
    }

    @Test
    void testConstructorWithUsuarioJwtAndTwoFactorCode() {
        loginDTO = new LoginDTO(usuario, jwt, true);

        assertEquals(usuario, loginDTO.getUsuario());
        assertEquals(jwt, loginDTO.getJwt());
        assertTrue(loginDTO.isHasTwoFactorCode());  // Tiene código de dos factores
    }

    @Test
    void testNoArgsConstructor() {
        loginDTO = new LoginDTO();

        assertNull(loginDTO.getUsuario());
        assertNull(loginDTO.getJwt());
        assertFalse(loginDTO.isHasTwoFactorCode());  // Por defecto debería ser false
    }

    @Test
    void testSettersAndGetters() {
        loginDTO = new LoginDTO();

        // Verificar los métodos setter y getter
        loginDTO.setUsuario(usuario);
        loginDTO.setJwt(jwt);
        loginDTO.setHasTwoFactorCode(true);

        assertEquals(usuario, loginDTO.getUsuario());
        assertEquals(jwt, loginDTO.getJwt());
        assertTrue(loginDTO.isHasTwoFactorCode());
    }

    @Test
    void testHasTwoFactorCodeDefaultValue() {
        loginDTO = new LoginDTO(usuario, jwt);

        // Verificar que el valor por defecto de hasTwoFactorCode sea false
        assertFalse(loginDTO.isHasTwoFactorCode());
    }

    @Test
    void testHasTwoFactorCodeSetToTrue() {
        loginDTO = new LoginDTO(usuario, jwt);
        loginDTO.setHasTwoFactorCode(true);

        // Verificar que hasTwoFactorCode sea true después de ser seteado
        assertTrue(loginDTO.isHasTwoFactorCode());
    }
}
