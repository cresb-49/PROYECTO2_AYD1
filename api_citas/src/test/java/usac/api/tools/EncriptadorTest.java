/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.tools;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Luis Monterroso
 */
public class EncriptadorTest {

    @InjectMocks
    private Encriptador encriptador;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para verificar que una contraseña se encripta correctamente.
     */
    @Test
    void testEncriptar() {
        String password = "miPasswordSegura";

        // Ejecutar el método de encriptación
        String passwordEncriptada = encriptador.encriptar(password);

        // Verificar que la contraseña encriptada no es igual a la original
        assertNotEquals(password, passwordEncriptada);

        // Verificar que la contraseña encriptada no sea nula
        assertNotNull(passwordEncriptada);
    }

    /**
     * Prueba para verificar que la comparación de contraseñas funciona
     * correctamente cuando la contraseña es válida.
     */
    @Test
    void testCompararPassword_Success() {
        String password = "miPasswordSegura";
        String passwordEncriptada = encriptador.encriptar(password);

        // Comparar la contraseña original con la encriptada
        boolean coincide = encriptador.compararPassword(password, passwordEncriptada);

        // Verificar que las contraseñas coinciden
        assertTrue(coincide);
    }

    /**
     * Prueba para verificar que la comparación de contraseñas falla
     * correctamente cuando la contraseña es incorrecta.
     */
    @Test
    void testCompararPassword_Failure() {
        String password = "miPasswordSegura";
        String passwordEncriptada = encriptador.encriptar(password);

        // Comparar con una contraseña incorrecta
        boolean coincide = encriptador.compararPassword("otraPassword", passwordEncriptada);

        // Verificar que las contraseñas no coinciden
        assertFalse(coincide);
    }
}
