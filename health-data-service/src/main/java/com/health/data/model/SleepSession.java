package com.health.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "sleep_sessions",
        indexes = @Index(name = "idx_user_timestamp_sleep", columnList = "userId, sleepStart")
)
public class SleepSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Integer sleepHours;

    private String sleepQuality;

    @Column(nullable = false)
    private LocalDateTime sleepStart;

    @Column(nullable = false)
    private LocalDateTime sleepEnd;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

}
