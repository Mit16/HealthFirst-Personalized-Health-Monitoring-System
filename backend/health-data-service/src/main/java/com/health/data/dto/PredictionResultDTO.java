package com.health.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResultDTO {
    private String riskLevel;
    private String precautions;
    private String possibleConditions;
    private String additionalNotes;
    private LocalDateTime timestamp;
}

