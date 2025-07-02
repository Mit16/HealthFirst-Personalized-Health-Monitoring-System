package com.health.data.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "heart_rate_metrics",
        indexes = @Index(name = "idx_user_timestamp_hr", columnList = "userId, timestamp")
)
public class HeartRateMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Float heartRate;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
