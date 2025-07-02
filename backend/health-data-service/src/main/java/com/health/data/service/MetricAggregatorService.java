package com.health.data.service;

import com.health.data.dto.PredictionInputDto;
import com.health.data.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MetricAggregatorService {

    private final HeartRateRepository heartRateRepo;
    private final SpO2Repository spo2Repo;
    private final TemperatureRepository tempRepo;
    private final StepRepository stepsRepo;
    private final SleepSessionRepository sleepRepo;

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
        LocalDate today = LocalDate.now(); // can enhance later
        return aggregateDailyPredictionInput(userId, today);
    }
}
