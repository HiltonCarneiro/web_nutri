package br.com.nutricionista.system.service;

import br.com.nutricionista.system.entity.Nutricionista;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PasswordResetEmailService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetEmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:no-reply@webnutri.local}")
    private String mailFrom;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${app.frontend.reset-password-path:/redefinir-senha}")
    private String resetPasswordPath;

    public void enviarEmailRedefinicao(
            Nutricionista nutricionista,
            String token,
            LocalDateTime dataExpiracao
    ) {
        String link = montarLink(token);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(nutricionista.getEmail());
        message.setSubject("Redefinição de senha - Web Nutri");
        message.setText(montarMensagem(nutricionista, link, dataExpiracao));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Falha ao enviar e-mail de redefinição para o nutricionista id={}", nutricionista.getId(), ex);
        }
    }

    private String montarLink(String token) {
        String baseUrl = frontendUrl != null ? frontendUrl.trim() : "";
        String path = resetPasswordPath != null ? resetPasswordPath.trim() : "/redefinir-senha";

        if (baseUrl.endsWith("/") && path.startsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else if (!baseUrl.endsWith("/") && !path.startsWith("/")) {
            path = "/" + path;
        }

        return baseUrl + path + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
    }

    private String montarMensagem(
            Nutricionista nutricionista,
            String link,
            LocalDateTime dataExpiracao
    ) {
        String expiracaoFormatada = dataExpiracao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        return """
                Olá, %s!

                Recebemos uma solicitação para redefinição da sua senha no sistema Web Nutri.

                Para criar uma nova senha, acesse o link abaixo:
                %s

                Este link expira em: %s

                Se você não solicitou essa redefinição, ignore este e-mail.

                Atenciosamente,
                Equipe Web Nutri
                """.formatted(nutricionista.getNome(), link, expiracaoFormatada);
    }
}