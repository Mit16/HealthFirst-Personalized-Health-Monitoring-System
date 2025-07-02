package com.health.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionInputDto {
    private Long userId;
    private Integer avgHeartRate;
    private Integer avgSpO2;
    private Double avgTemperature;
    private Integer totalSteps;
    private Integer sleepDurationMinutes;
}
