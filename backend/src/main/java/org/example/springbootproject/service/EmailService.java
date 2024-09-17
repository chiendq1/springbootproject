package org.example.springbootproject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import org.example.springbootproject.repository.ParameterRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final String DIGITS = "0123456789";
    private Map<String, String> dataCache = new HashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ParameterRepository parameterRepository;

    public void sendResetPasswordEmail(String contentName, String to, String subject) throws MessagingException {
        String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
        String otpCode = generateOTP(Constants.OTP_LENGTH);
        dataCache.put(to, otpCode);
        String htmlContent = String.format(htmlContentTemplate, to, otpCode);
        mailSender.send(this.createMimeMessage(to, Constants.SYSTEM_EMAIL, subject, htmlContent));
    }

    public String getOtpFromCache(String to) {
       return dataCache.get(to);
    }

    private MimeMessage createMimeMessage(String to, String from, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(from);
        message.setSubject(subject);
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        return message;
    }

    private String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return otp.toString();
    }
}
