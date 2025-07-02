package com.health.auth.controller;

import com.health.auth.model.*;
import com.health.auth.repository.UserRepository;
import com.health.auth.security.JwtUtil;
import com.health.auth.service.EmailService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private EmailService emailService;

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        User existingUser = userRepo.findByEmail(user.getEmail()).orElse(null);

        if (existingUser != null) {
            if (Boolean.TRUE.equals(existingUser.getVerified())){
                return ResponseEntity.badRequest().body("User already exist and verified");
            } else{
                String verificationToken = jwtUtil.generateToken(existingUser);
                existingUser.setVerificationToken(verificationToken);
                userRepo.save(existingUser);
                // Send google Email code
                emailService.sendVerificationEmail(existingUser.getEmail(), verificationToken);
                return ResponseEntity.ok("Verification Email resent. Check your inbox");
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String verificationToken = jwtUtil.generateToken(user);
        user.setVerificationToken(verificationToken);
        User savedUser = userRepo.save(user);

        //send google mail code
        emailService.sendForgotPasswordEmail(user.getEmail(), verificationToken);

        // Auto-create user profile
        createUserProfile(savedUser);

        return ResponseEntity.ok("User Registered Successfully! Please verify your Email");
    }

    private void createUserProfile(User user) {
        RestTemplate restTemplate = new RestTemplate();
        String userServiceUrl = "http://localhost:8082/user/profile/" + user.getId();

        UserProfileRequest profileRequest = new UserProfileRequest(
                user.getId(),
                user.getName(),
                user.getAge(),
                user.getGender(),
                user.getMedicalHistory()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer internal-service-token"); // ✅ Add this!
        HttpEntity<UserProfileRequest> request = new HttpEntity<>(profileRequest, headers);

        try {
            restTemplate.put(userServiceUrl, request);
            System.out.println("✅ User profile created successfully in user-service.");
        } catch (Exception e) {
            System.err.println("⚡ Failed to create user profile: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getVerified())) {
            return ResponseEntity.status(403).body("Account not verified. Please check your email.");
        }

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getName(), user.getEmail()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        var userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        var user = userOptional.get();
        String resetToken = jwtUtil.generateToken(user);
        user.setResetToken(resetToken);
        userRepo.save(user);

        emailService.sendForgotPasswordEmail(user.getEmail(), resetToken);

        return ResponseEntity.ok("Password reset email sent. Check your inbox.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String token,
                                         @PathVariable Long userId) {
        var user = userRepo.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new AlertResponse(user.getId(),user.getEmail())); // you can return full or a DTO if needed
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        var email = jwtUtil.extractEmail(token);
        var userOptional = userRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        var user = userOptional.get();

        if (!token.equals(user.getResetToken()) || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(403).body("Invalid or expired token");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepo.save(user);

        return ResponseEntity.ok("Password has been reset successfully.");
    }

}
