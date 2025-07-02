package com.health.ai_prediction_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.ai_prediction_service.dto.PredictionInputDto;
import com.health.ai_prediction_service.model.PredictionResult;
import com.health.ai_prediction_service.repository.PredictionRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${alerts.service.url}")
    private String alertsServiceUrl;

    @Value("${ai.api.url}")
    private String aiApiUrl;

    @Value("${ai.api.key}")
    private String aiApiKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final String geminiProModel = "gemini-2.0-flash";

    public PredictionResult predictHealth(PredictionInputDto metric) {
        try {
            String prompt = buildPrompt(metric);

            String responseBody = callAIModel(prompt);
            System.out.println("📩 Gemini raw response:\n" + responseBody);
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            String rawText = textNode.asText().trim();

            System.out.println("🔍 Sending prompt to Gemini:\n" + prompt);
            System.out.println("🧾 Payload:\n" + new ObjectMapper().writeValueAsString(buildGeminiRequestBody(prompt)));

            if (rawText.startsWith("```")) {
                rawText = rawText.replaceAll("(?s)```(?:json)?\\s*", "").replaceAll("```\\s*$", "").trim();
            }

            PredictionResult result = objectMapper.readValue(rawText, PredictionResult.class);

            result.setUserId(metric.getUserId());
            result.setTimestamp(LocalDateTime.now());
            repository.save(result);

            if ("HIGH".equalsIgnoreCase(result.getRiskLevel())) {
                triggerAlert(result);
            }

            System.out.println("🧾 Gemini Payload: " + new ObjectMapper().writeValueAsString(buildGeminiRequestBody(prompt)));

            return result;
        } catch (Exception e) {
            System.err.println("❌ Failed to generate prediction: " + e.getMessage());
            return PredictionResult.builder()
                    .userId(metric.getUserId())
                    .riskLevel("MODERATE")
                    .precautions("Stay hydrated, monitor vitals")
                    .possibleConditions("Unknown")
                    .additionalNotes("Default fallback due to error.")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }

    private String buildPrompt(PredictionInputDto dto) {
        return """
                Respond ONLY with a valid JSON object like this:
                {
                  "riskLevel": "HIGH",
                  "precautions": "Drink water, avoid stress",
                  "possibleConditions": "Hypertension",
                  "additionalNotes": "Consult a cardiologist if symptoms persist."
                }
                
                Do NOT include any markdown syntax or triple backticks.
                
                Input data:
                Average Heart Rate: %d
                Average SpO2: %d
                Average Temperature: %.2f
                Total Steps: %d
                Sleep Duration (mins): %d
                """.formatted(
                dto.getAvgHeartRate() != null ? dto.getAvgHeartRate() : 0,
                dto.getAvgSpO2() != null ? dto.getAvgSpO2() : 0,
                dto.getAvgTemperature() != null ? dto.getAvgTemperature() : 0.0,
                dto.getTotalSteps() != null ? dto.getTotalSteps() : 0,
                dto.getSleepDurationMinutes() != null ? dto.getSleepDurationMinutes() : 0
        );
    }


    private String callAIModel(String prompt) {
        WebClient client = WebClient.builder()
                .baseUrl(aiApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/" + geminiProModel + ":generateContent")
                        .queryParam("key", aiApiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON) // ✅ Add this
                .bodyValue(buildGeminiRequestBody(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    private Map<String, Object> buildGeminiRequestBody(String prompt) {
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );
    }


    public List<PredictionResult> getStoredPredictions(Long userId) {
        return repository.findByUserId(userId);
    }

    private void triggerAlert(PredictionResult result) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // ✅ Set Authorization header
            headers.setBearerAuth(generateInternalServiceToken());

            String body = """
                {
                    "userId": %d,
                    "riskLevel": "%s",
                    "precautions": "%s",
                    "possibleConditions": "%s",
                    "additionalNotes": "%s"
                }
                """.formatted(
                    result.getUserId(),
                    result.getRiskLevel(),
                    result.getPrecautions(),
                    result.getPossibleConditions(),
                    result.getAdditionalNotes()
            );

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(
                    alertsServiceUrl + "/api/alerts/trigger",
                    request,
                    String.class
            );

            System.out.println("📢 Alert triggered successfully for user " + result.getUserId());

        } catch (Exception e) {
            System.err.println("🚨 Failed to trigger alert: " + e.getMessage());
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
