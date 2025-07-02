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
        name = "accelerometer_logs",
        indexes = @Index(name = "idx_user_timestamp_accel", columnList = "userId, timestamp")
)
public class AccelerometerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Float accX;
    private Float accY;
    private Float accZ;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
