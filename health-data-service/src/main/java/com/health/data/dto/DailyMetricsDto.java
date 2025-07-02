package com.health.data.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMetricsDto {
    private String userId;
    private int steps;
    private Long sleepStartTime; // nullable
    private Long sleepEndTime;   // nullable
    private Long sleepDuration;  // nullable
    private String date;         // format: yyyy-MM-dd
}