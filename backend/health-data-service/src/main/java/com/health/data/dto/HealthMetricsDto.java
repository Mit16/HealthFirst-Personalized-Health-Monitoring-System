package com.health.data.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthMetricsDto {
    private String userId;
    private float heartRate;
    private float accX;
    private float accY;
    private float accZ;
    private float spo2;
    private float bodyTemperature;
    private long timestamp;
}