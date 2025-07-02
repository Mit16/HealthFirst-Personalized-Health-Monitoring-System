package com.health.data.service;


import com.health.data.dto.PredictionInputDto;
import com.health.data.dto.PredictionResultDTO;
import com.health.data.client.UserServiceClient;
import com.health.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
@RequiredArgsConstructor
@Service
public class HealthDataForwardingService {

    private final RestTemplate restTemplate;
    private final UserServiceClient userServiceClient;
    private final MetricAggregatorService metricAggregatorService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${prediction.service.url}")
    private String predictionServiceUrl;

    public void sendToAiPrediction(PredictionInputDto inputDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(generateInternalServiceToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PredictionInputDto> request = new HttpEntity<>(inputDto, headers);

        try {
            ResponseEntity<PredictionResultDTO> response = restTemplate.postForEntity(
                    predictionServiceUrl + "/api/prediction/predict",
                    request,
                    PredictionResultDTO.class
            );

            System.out.println("Prediction Risk Level: " + response.getBody().getRiskLevel());
        } catch (Exception e) {
            System.err.println("Error sending prediction input: " + e.getMessage());
        }
    }

    private String generateInternalServiceToken() {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject("service-account")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}