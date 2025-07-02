package com.health.ai_prediction_service.controller;

import com.health.ai_prediction_service.dto.PredictionResultDTO;
import com.health.ai_prediction_service.model.PredictionResult;
import com.health.ai_prediction_service.service.PredictionService;
import com.health.ai_prediction_service.dto.PredictionInputDto;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/prediction")
@RequiredArgsConstructor
public class PredictionController {
    private final PredictionService service;

    @PostMapping("/predict")
    public ResponseEntity<PredictionResultDTO> getPrediction(@RequestBody PredictionInputDto inputDto) {
        PredictionResult result = service.predictHealth(inputDto);
        return ResponseEntity.ok(PredictionResultDTO.builder()
                .riskLevel(result.getRiskLevel())
                .precautions(result.getPrecautions())
                .possibleConditions(result.getPossibleConditions())
                .additionalNotes(result.getAdditionalNotes())
                .timestamp(result.getTimestamp())
                .build());
    }



    @GetMapping("{userId}")
    public ResponseEntity<List<PredictionResult>> getPastMetrics(@PathVariable Long userId){
        List<PredictionResult> results = service.getStoredPredictions(userId);
        return ResponseEntity.ok(results);
    }

}