package com.health.auth.model;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdResponse {
    private Long userId;
    private String name;
}