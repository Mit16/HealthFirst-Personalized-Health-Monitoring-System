package com.health.alert_service.dto;

import lombok.Data;

@Data
public class AlertTriggerDTO {
    private Long userId;
    private String riskLevel;

    // 🔥 Add these fields to support full AI prediction
    private String precautions;
    private String possibleConditions;
    private String additionalNotes;
}
