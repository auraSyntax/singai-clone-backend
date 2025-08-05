package com.aura.syntax.pos.management.service.support;

import com.aura.syntax.pos.management.service.EmailData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public void sendHtmlMessage(EmailData emailData) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(emailData.getSubject());
        helper.setFrom(emailData.getFrom());
        helper.setText(emailData.getBody(), true);
        helper.setTo(emailData.getRecipients().toArray(new String[]{}));
        javaMailSender.send(mimeMessage);
    }
}
