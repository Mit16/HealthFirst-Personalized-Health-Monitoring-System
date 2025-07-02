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
        name = "spo2_metrics",
        indexes = @Index(name = "idx_user_timestamp_spo2", columnList = "userId, timestamp")
)
public class SpO2Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Float spo2;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
