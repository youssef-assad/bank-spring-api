package com.javaapp.api_banking.service.impl;

import com.javaapp.api_banking.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Our Bank API");
        message.setText(
                "Hello " + firstName + ",\n\n" +
                        "Your account has been successfully created.\n" +
                        "Welcome aboard!\n\n" +
                        "Bank Team"
        );
        mailSender.send(message);
    }
}
