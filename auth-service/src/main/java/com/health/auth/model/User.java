package com.health.auth.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String name;
    private int age;
    private String gender;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'None'")
    private String medicalHistory;


    private String verificationToken;

    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean verified = false;

    @Column(name = "reset_token")
    private String resetToken;
}