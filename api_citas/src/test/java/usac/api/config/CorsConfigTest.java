
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import usac.api.config.AppProperties;
import usac.api.config.CorsConfig;

/**
 * Pruebas unitarias para la clase CorsConfig. Verifica que la configuración
 * CORS se aplica correctamente.
 */
public class CorsConfigTest {

    @Mock
    private AppProperties appProperties;

    @Mock
    private CorsRegistry corsRegistry;

    @Mock
    private CorsRegistration corsRegistration;

    @InjectMocks
    private CorsConfig corsConfig;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Iniciar mocks
    }

    /**
     * Test que verifica la correcta configuración del CORS en la aplicación.
     */
    @Test
    void testAddCorsMappings() {
        // Simulamos el valor de la propiedad "hostFront" en AppProperties
        when(appProperties.getHostFront()).thenReturn("4200");

        // Simulamos el comportamiento del método addMapping que devuelve un CorsRegistration
        when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);

        // Simular el comportamiento encadenado de CorsRegistration
        when(corsRegistration.allowedOrigins(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(any())).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowCredentials(anyBoolean())).thenReturn(corsRegistration);

        // Llamar al método que configura CORS
        corsConfig.addCorsMappings(corsRegistry);

        // Verificamos que el método addMapping fue invocado con "/**"
        verify(corsRegistry, times(1)).addMapping("/**");

        // Verificamos que se permiten los orígenes correspondientes
        String expectedOrigin = "http://localhost:4200";
        verify(corsRegistration, times(1)).allowedOrigins(expectedOrigin);

        // Verificamos que los métodos permitidos están correctamente configurados
        verify(corsRegistration, times(1)).allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");

        // Verificamos que se permiten todos los headers
        verify(corsRegistration, times(1)).allowedHeaders("*");

        // Verificamos que las credenciales están permitidas
        verify(corsRegistration, times(1)).allowCredentials(true);
    }
}
