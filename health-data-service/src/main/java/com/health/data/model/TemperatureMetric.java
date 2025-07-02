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
        name = "temperature_metrics",
        indexes = @Index(name = "idx_user_timestamp_temp", columnList = "userId, timestamp")
)
public class TemperatureMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Float bodyTemperature;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
