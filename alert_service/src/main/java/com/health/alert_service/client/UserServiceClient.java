package com.health.alert_service.client;

import com.health.alert_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {

    @GetMapping("/user/profile/{userId}")
    UserResponse getUserProfile(@RequestHeader("Authorization") String token, @PathVariable("userId") Long userId);
}
