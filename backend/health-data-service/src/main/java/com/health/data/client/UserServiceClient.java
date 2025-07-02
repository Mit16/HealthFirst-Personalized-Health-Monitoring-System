package com.health.data.client;

import com.health.data.model.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


// This should match your UserService application name OR URL
@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {

    @GetMapping("/user/profile/{userId}")
    UserResponse getUserById(
            @PathVariable("userId") Long id,
            @RequestHeader("Authorization") String token
    );
}

