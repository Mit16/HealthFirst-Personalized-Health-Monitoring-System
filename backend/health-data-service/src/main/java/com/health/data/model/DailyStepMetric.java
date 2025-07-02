package com.health.data.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "daily_step_metrics",
        indexes = @Index(name = "idx_user_timestamp_steps", columnList = "userId, timestamp")
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStepMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Integer steps;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
