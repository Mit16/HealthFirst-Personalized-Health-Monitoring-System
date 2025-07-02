package com.health.ai_prediction_service.dto;

import lombok.Data;

@Data
public class PredictionInputDto {
    private Long userId;
    private Integer avgHeartRate;
    private Integer avgSpO2;
    private Double avgTemperature;
    private Integer totalSteps;
    private Integer sleepDurationMinutes;
}
