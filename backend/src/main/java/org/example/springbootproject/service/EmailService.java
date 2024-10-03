package org.example.springbootproject.service;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootproject.entity.User;
import org.example.springbootproject.repository.ParameterRepository;
import org.example.springbootproject.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmailService extends BaseService{
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
        String[] emailTo = {to};
        mailSender.send(this.createMimeMessage(emailTo, Constants.SYSTEM_EMAIL, "", subject, htmlContent, null, null));
    }

    @Async
    public void sendCreateAccountEmail(String contentName, String to, String subject, String password) throws MessagingException {
        String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
        String htmlContent = String.format(htmlContentTemplate, to, password);
        String[] emailTo = {to};
        mailSender.send(this.createMimeMessage(emailTo, Constants.SYSTEM_EMAIL, "", subject, htmlContent, null, null));
    }

    public void sendEmailContract(String contentName, Map<String, String> data, String[] to) throws MessagingException {
        String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
        String htmlContent = String.format(htmlContentTemplate, data.get("tenants"), data.get("roomCode"), data.get("terminateDate"), Constants.SYSTEM_NAME);
        mailSender.send(this.createMimeMessage(to, Constants.SYSTEM_EMAIL, data.get("cc"), data.get("subject"), htmlContent, null, null));
    }

    @Async
    public void sendEmailBill(String contentName, Map<String, Object> data, ByteArrayOutputStream fileContent) throws MessagingException {
        String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
        String fileName = data.get("date").toString() + data.get("roomCode").toString();
        List<User> tenants = (List<User>) data.get("tenants");
        String users = tenants.stream().map(User::getFullName).collect(Collectors.joining(", "));
        String htmlContent = String.format(htmlContentTemplate, users, data.get("roomCode").toString(), data.get("date").toString(), Constants.SYSTEM_NAME);
        String[] emailTo = tenants.stream().map(User::getEmail).toArray(String[]::new);
        mailSender.send(this.createMimeMessage(emailTo, Constants.SYSTEM_EMAIL, "", "Room Bill Notice", htmlContent, fileName, fileContent));
    }

    public void sendEmailBillOverdue(String contentName, Map<String, Object> data) {
        try {
            String htmlContentTemplate = parameterRepository.findByName(contentName).getValue();
            Set<User> tenants = (Set<User>) data.get("tenants");
            String users = tenants.stream().map(User::getFullName).collect(Collectors.joining(", "));
            String htmlContent = String.format(htmlContentTemplate, users, data.get("roomCode").toString(),data.get("date") ,data.get("overdue").toString(), Constants.SYSTEM_NAME);
            String[] emailTo = tenants.stream().map(User::getEmail).toArray(String[]::new);
            mailSender.send(this.createMimeMessage(emailTo, Constants.SYSTEM_EMAIL, "", "Room Overdue Bill", htmlContent, null, null));
            logger.info("Send email success");
        } catch(MessagingException ex) {
            logger.error(ex.getMessage());
        }
    }

    private MimeMessage createMimeMessage(String[] to, String from, String cc, String subject, String htmlContent, String fileName, ByteArrayOutputStream fileContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setTo(to);
        if (!cc.isEmpty()) helper.setCc(cc);
        helper.setText("", htmlContent);
        if (fileContent != null && fileName != null) {
            DataSource dataSource = new ByteArrayDataSource(fileContent.toByteArray(), "application/pdf");
            helper.addInline(fileName, dataSource);
        }
        return message;
    }

    public String getOtpFromCache(String to) {
        return dataCache.get(to);
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
