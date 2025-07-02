package com.health.auth.controller;

import com.health.auth.model.*;
import com.health.auth.repository.UserRepository;
import com.health.auth.repository.WearTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/wear")
@RequiredArgsConstructor
public class WearIntegrationController {

    private final UserRepository userRepo;
    private final WearTokenRepository tokenRepo;

    /**
     * Called by website to generate pairing token (shown to user)
     */
    @GetMapping("/generate-token")
    public ResponseEntity<WearTokenResponse> generateToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User user = userRepo.findByEmail(email).orElseThrow();

        String token = UUID.randomUUID().toString();
        WearToken wearToken = WearToken.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        tokenRepo.save(wearToken);
        return ResponseEntity.ok(new WearTokenResponse(token));
    }

    /**
     * Called by Wear OS app to link using token
     */
    @PostMapping("/link-wear-device")
    public ResponseEntity<UserIdResponse> linkWearDevice(@RequestBody WearLinkRequest request) {
        WearToken tokenEntry = tokenRepo.findById(request.getToken()).orElse(null);
        System.out.println("Wear device linking requested with token: " + request.getToken());


        if (tokenEntry == null) {
            return ResponseEntity.status(403).body(new UserIdResponse(null, "Token not found"));
        }

        if (tokenEntry.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepo.deleteById(request.getToken()); // ⛔ Optional: delete expired token
            return ResponseEntity.status(403).body(new UserIdResponse(null, "Token expired"));
        }

        User user = tokenEntry.getUser();
        return ResponseEntity.ok(new UserIdResponse(user.getId(), user.getName()));
    }

}