package com.health.user.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private Long userId;

    private String name;
    private int age;
    private String gender;

    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'None'")
    private String medicalHistory;


}

