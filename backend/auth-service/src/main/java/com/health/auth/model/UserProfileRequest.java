package com.health.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {
    private Long userId;
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;
}
