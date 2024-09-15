package com.example.ticketing.service;

import com.example.ticketing.repository.EmailRepository;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    //@Autowired
    private EmailRepository emailRepository = new EmailRepository();

    public void sendEmail(String to, String subject, String content) {
        Email from = new Email("levaldas@hotmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            // Jei el. laiškas sėkmingai išsiųstas, įrašykite sėkmės būseną į DB
            emailRepository.saveEmailLog(to, subject, "SENT", null);
        } catch (IOException ex) {
            // Jei nepavyko, įrašykite klaidos pranešimą
            emailRepository.saveEmailLog(to, subject, "FAILED", ex.getMessage());
        }
    }
}

