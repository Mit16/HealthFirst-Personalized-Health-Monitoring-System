package com.health.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Deprecated
public class HealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer heartRate;
    private Integer spo2;
    private Float bodyTemperature;
    private Integer steps;
    private Float sleepHours;

    private LocalDateTime timestamp;
}
