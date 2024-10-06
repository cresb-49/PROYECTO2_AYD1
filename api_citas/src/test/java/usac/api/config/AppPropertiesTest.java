package usac.api.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Clase de pruebas unitarias para la clase AppProperties. Se verifica la
 * correcta inyección y funcionamiento de las propiedades de configuración.
 */
@SpringBootTest(classes = AppProperties.class)
@EnableConfigurationProperties(AppProperties.class)
@ActiveProfiles("test")  // Si tienes un perfil de configuración específico para los tests
public class AppPropertiesTest {

    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        appProperties = new AppProperties();
    }

    /**
     * Prueba para verificar que se puede establecer y obtener la propiedad
     * "hostFront".
     */
    @Test
    void testSetAndGetHostFront() {
        // Establecer el valor de hostFront
        String expectedHostFront = "http://localhost:4200";
        appProperties.setHostFront(expectedHostFront);

        // Verificar que se puede obtener el valor correctamente
        assertEquals(expectedHostFront, appProperties.getHostFront());
    }

    /**
     * Prueba para verificar el valor por defecto de la propiedad "hostFront".
     */
    @Test
    void testDefaultHostFront() {
        // Verificar que el valor por defecto de hostFront es null antes de la configuración
        assertEquals(null, appProperties.getHostFront());
    }
}
