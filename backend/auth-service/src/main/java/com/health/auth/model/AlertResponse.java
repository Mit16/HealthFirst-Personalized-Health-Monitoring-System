package com.health.auth.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponse {
    private Long userId;
    private String email;
}
