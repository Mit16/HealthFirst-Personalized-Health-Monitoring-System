package com.health.auth.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "wear_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WearToken {

    @Id
    private String token; // UUID or secure string

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
