package com.health.alert_service.client;

import com.health.alert_service.dto.UserAuthDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://localhost:8081")
public interface AuthServiceClient {

    @GetMapping("/auth/user/{userId}")
    UserAuthDTO getUserById(@RequestHeader("Authorization") String token, @PathVariable("userId") Long userId);
}
