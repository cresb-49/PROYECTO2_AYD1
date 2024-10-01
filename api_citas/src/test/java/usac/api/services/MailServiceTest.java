/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import org.thymeleaf.context.Context;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import usac.api.config.AppProperties;
import usac.api.repositories.UsuarioRepository;

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
        when(appProperties.getHostFront()).thenReturn("8080");

        // Simulamos la creación del contexto y el procesamiento del template
        IContext context = new Context();  // Context implementa IContext
        when(templateEngine.process(eq("CorreoDeRecuperacion"),
                any(IContext.class)))
                .thenReturn("<html>Correo de recuperación</html>");

        // Simulamos la creación del mensaje MIME
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper mimeMessageHelper = mock(MimeMessageHelper.class);
        MimeMessageHelper spyHelper = spy(new MimeMessageHelper(mimeMessage, "utf-8"));

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
        when(templateEngine.process(eq("CorreoDeRecuperacion"),
                any(Context.class)))
                .thenReturn("<html>Correo de recuperación</html>");  // Aseguramos que no sea null

        // Simulamos una excepción al enviar el mensaje
        Mockito.doThrow(new RuntimeException("Error al enviar el mensaje"))
                .when(mailSender).send(any(MimeMessage.class));

        // Ejecutamos el método (la excepción debe ser manejada internamente)
        mailService.enviarCorreoDeRecuperacion(correo, codigoActivacion);

        // Verificamos que se intentó enviar el correo pero falló
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

}
