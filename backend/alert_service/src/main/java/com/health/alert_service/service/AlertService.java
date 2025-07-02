package com.health.alert_service.service;

import com.health.alert_service.client.UserServiceClient;
import com.health.alert_service.client.AuthServiceClient;
import com.health.alert_service.dto.AlertTriggerDTO;
import com.health.alert_service.model.Alert;
import com.health.alert_service.notification.EmailNotifier;
import com.health.alert_service.repository.AlertRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository repository;
    private final EmailNotifier emailNotifier;
    // Add to constructor
    private final UserServiceClient userServiceClient;
    private final AuthServiceClient authServiceClient;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private String generateInternalServiceToken() {
        return Jwts.builder()
                .setSubject("alert-service")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<Alert> getAlertsByUserId(Long userId) {
        return repository.findByUserId(userId);
    }


    public void triggerAlert(AlertTriggerDTO dto) {
        if ("HIGH".equalsIgnoreCase(dto.getRiskLevel())) {
            Alert alert = Alert.builder()
                    .userId(dto.getUserId())
                    .message("🚨 High health risk detected!")
                    .timestamp(LocalDateTime.now())
                    .build();
            repository.save(alert);

            try {
                String token = "Bearer " + generateInternalServiceToken();
                var userAuth = authServiceClient.getUserById(token, dto.getUserId());
                var userProfile = userServiceClient.getUserProfile(token, dto.getUserId());

                if (userAuth.getEmail() != null) {
                    String message = """
            <strong>Patient:</strong> %s<br/>
            <strong>Age:</strong> %s<br/>
            <strong>Gender:</strong> %s<br/><br/>
            <strong>⚠️ Prediction:</strong><br/>
            <strong>Risk Level:</strong> %s<br/>
            <strong>Precautions:</strong> %s<br/>
            <strong>Possible Conditions:</strong> %s<br/>
            <strong>Notes:</strong> %s<br/>
            """.formatted(
                            userProfile.getName(),
                            userProfile.getAge(),
                            userProfile.getGender(),
                            dto.getRiskLevel(),
                            dto.getPrecautions(),
                            dto.getPossibleConditions(),
                            dto.getAdditionalNotes()
                    );

                    emailNotifier.send(userAuth.getEmail(), message);
                }

            } catch (Exception e) {
                System.err.println("⚠️ Failed to send alert email: " + e.getMessage());
            }
        }

    }

}

