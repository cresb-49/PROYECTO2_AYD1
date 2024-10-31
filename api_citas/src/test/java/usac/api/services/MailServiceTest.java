package usac.api.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import usac.api.config.AppProperties;

/**
 *
 * @author Luis Monterroso
 */
public class MailServiceTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private AppProperties appProperties;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba para verificar que el correo de recuperación se envía
     * correctamente.
     */
    @Test
    void testEnviarCorreoDeRecuperacion_Success() throws MessagingException {
        String correo = "test@example.com";
        String codigoActivacion = "123456";

        // Simulamos el comportamiento de las propiedades de la aplicación
        when(appProperties.getHostFrontDev()).thenReturn("8080");

        // Simulamos la creación del contexto y el procesamiento del template
        IContext context = new Context();  // Context implementa IContext
        when(templateEngine.process(eq("CorreoDeRecuperacion"), any(IContext.class)))
                .thenReturn("<html>Correo de recuperación</html>");

        // Simulamos la creación del mensaje MIME
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Ejecutar el método
        mailService.enviarCorreoDeRecuperacion(correo, codigoActivacion);

        // Verificar que se procesó el template correctamente
        verify(templateEngine, times(1)).process(eq("CorreoDeRecuperacion"), any(IContext.class));

        // Verificar que se llamó al envío del correo
        verify(mailSender, times(1)).send(mimeMessage);
    }

    /**
     * Prueba que verifica que se lanza una excepción cuando falla el envío del
     * correo de recuperación.
     */
    @Test
    void testEnviarCorreoDeRecuperacion_Failure() throws MessagingException {
        String correo = "test@example.com";
        String codigoActivacion = "123456";

        // Simulamos la creación correcta del MimeMessage
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Simulamos que el template engine procesa correctamente el template
        when(templateEngine.process(eq("CorreoDeRecuperacion"), any(Context.class)))
                .thenReturn("<html>Correo de recuperación</html>");

        // Simulamos una excepción al enviar el mensaje
        Mockito.doThrow(new RuntimeException("Error al enviar el mensaje"))
                .when(mailSender).send(any(MimeMessage.class));

        // Ejecutamos el método (la excepción debe ser manejada internamente)
        mailService.enviarCorreoDeRecuperacion(correo, codigoActivacion);

        // Verificamos que se intentó enviar el correo pero falló
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    /**
     * Prueba para verificar que el correo de autenticación de token se envía
     * correctamente.
     */
    @Test
    void testEnviarCorreoTokenAuth_Success() throws MessagingException {
        String correo = "test@example.com";
        String codigo = "123456";

        // Simulamos el comportamiento del procesamiento del template
        when(templateEngine.process(eq("CorreoTokenAuth"), any(Context.class)))
                .thenReturn("<html>Correo de autenticación</html>");

        // Simulamos la creación del mensaje MIME
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Ejecutar el método
        mailService.enviarCorreoTokenAuth(correo, codigo);

        // Verificar que se procesó el template correctamente
        verify(templateEngine, times(1)).process(eq("CorreoTokenAuth"), any(Context.class));

        // Verificar que se llamó al envío del correo
        verify(mailSender, times(1)).send(mimeMessage);
    }

}
