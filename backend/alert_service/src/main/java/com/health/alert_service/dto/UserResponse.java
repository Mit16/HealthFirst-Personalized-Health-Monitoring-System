package com.health.alert_service.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String name;
    private int age;
    private String gender;
}
