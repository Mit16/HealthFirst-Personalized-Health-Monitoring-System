package com.health.ai_prediction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertTriggerDTO {
    private Long userId;
    private String riskLevel;

    // 🔥 Add these fields to support full AI prediction
    private String precautions;
    private String possibleConditions;
    private String additionalNotes;

}
