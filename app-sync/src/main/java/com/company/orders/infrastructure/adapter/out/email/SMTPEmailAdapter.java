package com.company.orders.infrastructure.adapter.out.email;

import com.company.orders.domain.port.out.EmailNotificationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Properties;

@Component
public class SMTPEmailAdapter implements EmailNotificationPort {
    
    @Value("${email.smtp.host}")
    private String smtpHost;
    
    @Value("${email.smtp.port}")
    private int smtpPort;
    
    @Value("${email.smtp.username:}")
    private String smtpUsername;
    
    @Value("${email.smtp.password:}")
    private String smtpPassword;
    
    @Value("${email.smtp.api-key:}")
    private String apiKey;
    
    @Value("${email.smtp.use-ssl:false}")
    private boolean useSsl;
    
    @Value("${email.smtp.use-starttls:true}")
    private boolean useStartTls;
    
    @Value("${email.from:orders@company.com}")
    private String fromEmail;
    
    private JavaMailSender mailSender;
    
    @Override
    public Mono<Void> sendEmail(String to, String subject, String body) {
        return Mono.fromRunnable(() -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            getMailSender().send(message);
        });
    }
    
    @Override
    public Mono<Void> sendOrderNotification(String to, String orderName, Double total) {
        String subject = "Order Confirmation - " + orderName;
        String body = String.format(
            "Dear Customer,\n\n" +
            "Your order '%s' has been successfully created.\n" +
            "Total amount: $%.2f\n\n" +
            "Thank you for your purchase!\n" +
            "Best regards,\n" +
            "Orders Team",
            orderName, total
        );
        
        return sendEmail(to, subject, body);
    }
    
    private JavaMailSender getMailSender() {
        if (mailSender == null) {
            mailSender = createMailSender();
        }
        return mailSender;
    }
    
    private JavaMailSender createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(smtpHost);
        sender.setPort(smtpPort);
        
        if (!apiKey.isEmpty()) {
            sender.setUsername(apiKey);
            sender.setPassword(apiKey);
        } else {
            sender.setUsername(smtpUsername);
            sender.setPassword(smtpPassword);
        }
        
        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", String.valueOf(useSsl));
        props.put("mail.smtp.starttls.enable", String.valueOf(useStartTls));
        
        return sender;
    }
}
