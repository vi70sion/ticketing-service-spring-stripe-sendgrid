package com.example.ticketing.controller;

import com.example.ticketing.service.EmailService;
import com.example.ticketing.service.NewslettersService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class EmailController {

    NewslettersService newslettersService = new NewslettersService();
    @Autowired
    private EmailService emailService = new EmailService();

    public EmailController() { }

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @PostMapping("/ticketing/send-confirmation-email")
    public ResponseEntity<String> sendConfirmationEmail(@RequestBody EmailRequest emailRequest) {
        Email from = new Email("levaldas@hotmail.com"); // verified sender email
        String subject = "Pirkimo patvirtinimas";
        Email to = new Email(emailRequest.getUserEmail());
        Content content = new Content("text/plain", "Apmokėjimas sėkmingas. Ačiū už jūsų pirkinį!");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() == 202) {
                return ResponseEntity.ok("El.paštas nusiųstas sėkmingai.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Nepavyko nusiųsti el.pašto: " + response.getBody());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nepavyko nusiųsti el.pašto: " + e.getMessage());
        }
    }

    @PostMapping("/ticketing/send-refund-email")
    public ResponseEntity<String> sendRefundEmail(@RequestBody EmailRequest emailRequest) {
        Email from = new Email("levaldas@hotmail.com"); // verified sender email
        String subject = "Pirkimo atšaukimas";
        Email to = new Email(emailRequest.getUserEmail());
        Content content = new Content("text/plain", "Jūsų mokėjimas buvo atšauktas ir grąžinimas inicijuotas.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() == 202) {
                return ResponseEntity.ok("Grąžinimo laiškas nusiųstas sėkmingai.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Nepavyko nusiųsti el.pašto: " + response.getBody());
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Nepavyko nusiųsti el.pašto: " + e.getMessage());
        }
    }

    @PostMapping("/ticketing/subscribe")
    public ResponseEntity<String> subscribe(@RequestBody EmailRequest emailRequest) {
        return (newslettersService.subscribe(emailRequest.getUserEmail()))
                ? ResponseEntity.ok("Užsakyta sėkmingai")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nepavyko");
    }


    public static class EmailRequest {
        private String userEmail;
        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    }

    @GetMapping("/ticketing/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        emailService.sendEmail(to, subject, content);
        return "El.paštas išsiųstas!";
    }

}