/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import usac.api.config.AppProperties;
import usac.api.repositories.UsuarioRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class MailService extends Service {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private AppProperties appProperties;

    public void enviarCorreoDeRecuperacion(String correo, String codigoActivacion) {
        try {
            Context context = new Context();//crear nuevo contexto
            String url = String.format("http://localhost:%s/password_reset/form?c=%s",
                    appProperties.getHostFrontDev(), codigoActivacion);//TODO VErificar cuando es prod y cuando es dev

            context.setVariable("url", url);//adjuntar las variables     
            String html = templateEngine.process("CorreoDeRecuperacion", context);
            //mandamos el correo electronico
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(html, true);//adjuntamos el mansaje indicando que sera un html
            helper.setTo(correo);
            helper.setSubject("Recuperación de cuenta P2.");
            helper.setFrom("P2 <namenotfound4004@gmail.com>");
            mailSender.send(mimeMessage);
        } catch (Exception ex) {
        }
    }

    public void enviarCorreoTokenAuth(String correo, String codigo) {
        try {
            Context context = new Context();//crear nuevo contexto
            context.setVariable("codigo", codigo);//adjuntar las variables     
            String html = templateEngine.process("CorreoTokenAuth", context);
            //mandamos el correo electronico
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(html, true);//adjuntamos el mansaje indicando que sera un html
            helper.setTo(correo);
            helper.setSubject("Token creacion de cuenta P2.");
            helper.setFrom("P2 <namenotfound4004@gmail.com>");
            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
