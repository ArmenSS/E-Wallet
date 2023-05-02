package com.wallet.mailservice.service.impl;

import com.wallet.mailservice.dto.MailVerifyDto;
import com.wallet.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailServiceImpl implements MailService {

    private final MailSender mailSender;
//    private final TemplateEngine templateEngine;

    @Override
    public void sendEmail(MailVerifyDto mailVerifyDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailVerifyDto.getEmail());
        mailMessage.setSubject("verification email");
        mailMessage.setText(mailVerifyDto.getMailVerificationLink());

        mailSender.send(mailMessage);
    }

    @Override
    @Async
    public void sendHtmlMessage(MailVerifyDto mailVerifyDto) {
        try {
            String from = "poxo5yanp@yandex.ru";

            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", "smtp.yandex.ru");
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            properties.setProperty("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "Aa1234567!a");
                }
            });

            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("mail verification");
            helper.setFrom(from);
            helper.setTo(mailVerifyDto.getEmail());
            helper.setReplyTo(from);
            String url = "<html><body><p>Click <a href=\"" + mailVerifyDto.getMailVerificationLink() + "\">here</a> to visit our website</p></body></html>";
            helper.setText(url, true);
            Transport.send(message);
            System.out.println("Email Sent successfully....");

        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}