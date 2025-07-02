package com.health.data.controller;

import com.health.data.dto.DailyMetricsDto;
import com.health.data.dto.HealthMetricsDto;
import com.health.data.dto.PredictionInputDto;
import com.health.data.service.HealthDataForwardingService;
import com.health.data.service.HealthMetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Slf4j
public class HealthMetricsController {

    private final HealthMetricsService healthMetricsService;
    private final HealthDataForwardingService healthDataForwardingService;

    @PostMapping("/health")
    public ResponseEntity<Void> receiveHealthMetrics(@RequestBody HealthMetricsDto dto) {
        healthMetricsService.saveHealthMetrics(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/daily")
    public ResponseEntity<Void> receiveDailyMetrics(@RequestBody DailyMetricsDto dto) {
        healthMetricsService.saveDailyMetrics(dto);
        return ResponseEntity.ok().build();
    }

    // New: Used by AI Prediction Service to fetch aggregate metrics
    @GetMapping("/prediction-input/{userId}")
    public ResponseEntity<PredictionInputDto> getPredictionInput(@PathVariable Long userId) {
        PredictionInputDto input = healthMetricsService.getLatestPredictionInput(userId);
        return ResponseEntity.ok(input);
    }

    // New: For viewing stored raw metrics
    @GetMapping("/raw/{userId}")
    public ResponseEntity<?> getRawMetrics(@PathVariable Long userId) {
        return ResponseEntity.ok(healthMetricsService.getAllRawMetrics(userId));
    }

    // New: For viewing stored daily metrics
    @GetMapping("/daily-view/{userId}")
    public ResponseEntity<?> getDailyMetrics(@PathVariable Long userId) {
        return ResponseEntity.ok(healthMetricsService.getAllDailyMetrics(userId));
    }
}
