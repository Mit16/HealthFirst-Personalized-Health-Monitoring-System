package com.health.auth.service;

import com.health.auth.model.WearToken;
import com.health.auth.repository.WearTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class WearTokenCleanupService {

    private final WearTokenRepository tokenRepo;

    public WearTokenCleanupService(WearTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanExpiredTokens() {
        List<WearToken> allTokens = tokenRepo.findAll();

        allTokens.stream()
                .filter(t -> t.getExpiresAt().isBefore(LocalDateTime.now()))
                .forEach(t -> {
                    tokenRepo.deleteById(t.getToken());
                    System.out.println("Deleted expired token: " + t.getToken());
                });
    }
}