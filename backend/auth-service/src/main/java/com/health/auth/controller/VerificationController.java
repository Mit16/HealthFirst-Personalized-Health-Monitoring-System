package com.health.auth.controller;

import com.health.auth.model.User;
import com.health.auth.repository.UserRepository;
import com.health.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/auth/signup/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        String emailString = jwtUtil.extractEmail(token);
        var userOptional = userRepository.findByEmail(emailString);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token or user not found");
        }

        var user = userOptional.get();

        if (!jwtUtil.validateToken(token) || !token.equals(user.getVerificationToken())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token expired or invalid");
        }

        user.setVerificationToken(null);
        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Email successfully verified!");
    }
}

