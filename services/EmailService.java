package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.repository.MerchantRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    MerchantRepository merchantRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async
    public void sendEmail(String to, String subject, String body){
        log.info("Starting email sending process to {}...",to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("trendhivedev@gmail.com");
        mailSender.send(message);
        log.info("Email send successfully to user {}...",to);
    }

    @Async
    public void sendOtpEmail(String to, String otp) {
        log.info("Starting email sending process to {}...",to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP");
        message.setText("Your OTP is: " + otp);
        message.setFrom("trendhivedev@gmail.com");
        mailSender.send(message);
        log.info("Email send successfully to user {}...",to);
    }
}
