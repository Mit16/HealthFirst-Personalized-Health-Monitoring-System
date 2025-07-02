package com.health.alert_service.notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotifier {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromSender;

    public void send(String toEmail, String messageBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setFrom(fromSender);
            helper.setSubject("🚨 Health Alert Notification");

            String html = """
                <div style="font-family: Arial; padding: 20px; background-color: #fff; border-radius: 8px;">
                    <h2 style="color: red;">⚠️ High Health Risk Alert</h2>
                    <p>%s</p>
                    <p style="color: #999; font-size: 12px;">This is an automated message.</p>
                </div>
            """.formatted(messageBody);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("❌ Failed to send alert email: " + e.getMessage());
        }
    }
}
