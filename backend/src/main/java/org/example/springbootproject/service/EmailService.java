package org.example.springbootproject.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.springbootproject.repository.ParameterRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
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

    @Async
    public void sendCreateAccountEmail(String contentName, String to, String subject, String password) throws MessagingException {
        String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
        String htmlContent = String.format(htmlContentTemplate, to, password);
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
            otp.append(Constants.DIGITS.charAt(random.nextInt(Constants.DIGITS.length())));
        }
        return otp.toString();
    }

    public String generatePassword() {
        String combinedChars = Constants.UPPERCASE + Constants.LOWERCASE + Constants.DIGITS + Constants.SPECIAL_CHARACTERS;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(Constants.PASSWORD_MIN_LENGTH);

        // Ensure password contains at least one character from each category
        password.append(Constants.UPPERCASE.charAt(random.nextInt(Constants.UPPERCASE.length())));
        password.append(Constants.LOWERCASE.charAt(random.nextInt(Constants.LOWERCASE.length())));
        password.append(Constants.DIGITS.charAt(random.nextInt(Constants.DIGITS.length())));
        password.append(Constants.SPECIAL_CHARACTERS.charAt(random.nextInt(Constants.SPECIAL_CHARACTERS.length())));

        // Fill the remaining characters
        for (int i = 4; i < Constants.PASSWORD_MIN_LENGTH; i++) {
            password.append(combinedChars.charAt(random.nextInt(combinedChars.length())));
        }

        return password.toString();
    }
}
