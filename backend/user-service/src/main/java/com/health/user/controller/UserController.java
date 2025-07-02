package com.health.user.controller;


import com.health.user.model.UserProfile;
import com.health.user.security.JwtUtil;
import com.health.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService service;
    private final JwtUtil jwtUtil;

    @GetMapping("/profile/{userId}")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token,
                                        @PathVariable Long userId) {
        // Validate token if needed
        return ResponseEntity.ok(service.getUserProfile(userId));
    }


    @PutMapping("/profile/{userId}")
    public ResponseEntity<?> updateProfile(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @RequestBody UserProfile updatedProfile) {

        updatedProfile.setUserId(userId);
        return ResponseEntity.ok(service.updateUserProfile(updatedProfile));
    }

}