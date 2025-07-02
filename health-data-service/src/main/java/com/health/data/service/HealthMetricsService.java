package com.health.data.service;

import com.health.data.dto.DailyMetricsDto;
import com.health.data.dto.HealthMetricsDto;
import com.health.data.dto.PredictionInputDto;
import com.health.data.model.*;
import com.health.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthMetricsService {

    private final AccelerometerLogRepository accelRepo;
    private final HeartRateRepository heartRateRepo;
    private final SpO2Repository spo2Repo;
    private final TemperatureRepository tempRepo;
    private final StepRepository stepsRepo;
    private final SleepSessionRepository sleepRepo;
    private final HealthDataForwardingService healthDataForwardingService;
    private final MetricAggregatorService metricAggregatorService;

    public void saveHealthMetrics(HealthMetricsDto dto) {
        Long userId = Long.parseLong(dto.getUserId());
        LocalDateTime timestamp = toLocalDateTime(dto.getTimestamp());

        heartRateRepo.save(HeartRateMetric.builder()
                .userId(userId)
                .heartRate(dto.getHeartRate())
                .timestamp(timestamp)
                .build());

        spo2Repo.save(SpO2Metric.builder()
                .userId(userId)
                .spo2(dto.getSpo2())
                .timestamp(timestamp)
                .build());

        accelRepo.save(AccelerometerLog.builder()
                .userId(userId)
                .accX(dto.getAccX())
                .accY(dto.getAccY())
                .accZ(dto.getAccZ())
                .timestamp(timestamp)
                .build());

        tempRepo.save(TemperatureMetric.builder()
                .userId(userId)
                .bodyTemperature(dto.getBodyTemperature())
                .timestamp(timestamp)
                .build());
    }

    public void saveDailyMetrics(DailyMetricsDto dto) {
        Long userId = Long.parseLong(dto.getUserId());
        LocalDate date = LocalDate.parse(dto.getDate());
        LocalDateTime timestamp = date.atStartOfDay();

        stepsRepo.save(DailyStepMetric.builder()
                .userId(userId)
                .steps(dto.getSteps())
                .timestamp(timestamp)
                .build());


        if (dto.getSleepStartTime() != null && dto.getSleepEndTime() != null && dto.getSleepDuration() != null) {
            LocalDateTime sleepStart = toLocalDateTime(dto.getSleepStartTime());
            LocalDateTime sleepEnd = toLocalDateTime(dto.getSleepEndTime());
            int sleepMinutes = (int) (dto.getSleepDuration() / (1000 * 60));
         System.out.println("Start: " + sleepStart + " | End: " + sleepEnd + " | Minutes: " + sleepMinutes);

            sleepRepo.save(SleepSession.builder()
                    .userId(userId)
                    .sleepHours(sleepMinutes)
                    .sleepQuality("Normal")
                    .sleepStart(sleepStart)
                    .sleepEnd(sleepEnd)
                    .timestamp(LocalDateTime.now())
                    .build());
        }


        // 🔥 Trigger AI Prediction
        PredictionInputDto input = aggregateDailyPredictionInput(userId, date);
        healthDataForwardingService.sendToAiPrediction(input);
    }

    private LocalDateTime toLocalDateTime(long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public PredictionInputDto aggregateDailyPredictionInput(Long userId, LocalDate date) {
        Double avgHR = heartRateRepo.avgByUserAndDate(userId, date);
        Double avgTemp = tempRepo.avgByUserAndDate(userId, date);
        Double avgSpO2 = spo2Repo.avgByUserAndDate(userId, date);
        Integer steps = stepsRepo.findStepsByUserAndDate(userId, date);
        Integer sleepMinutes = sleepRepo.findSleepDurationByUserAndDate(userId, date);

        return new PredictionInputDto(
                userId,
                avgHR != null ? avgHR.intValue() : null,
                avgSpO2 != null ? avgSpO2.intValue() : null,
                avgTemp,
                steps,
                sleepMinutes
        );
    }

    public PredictionInputDto getLatestPredictionInput(Long userId) {
        return metricAggregatorService.getLatestPredictionInput(userId);
    }

    public Map<String, Object> getAllRawMetrics(Long userId) {
        return Map.of(
                "heartRate", heartRateRepo.findByUserIdOrderByTimestampDesc(userId),
                "spo2", spo2Repo.findByUserIdOrderByTimestampDesc(userId),
                "temperature", tempRepo.findByUserIdOrderByTimestampDesc(userId),
                "accelerometer", accelRepo.findByUserIdOrderByTimestampDesc(userId)
        );
    }

    public Map<String, Object> getAllDailyMetrics(Long userId) {
        return Map.of(
                "steps", stepsRepo.findByUserIdOrderByTimestampDesc(userId),
                "sleep", sleepRepo.findByUserIdOrderByTimestampDesc(userId)
        );
    }
}
