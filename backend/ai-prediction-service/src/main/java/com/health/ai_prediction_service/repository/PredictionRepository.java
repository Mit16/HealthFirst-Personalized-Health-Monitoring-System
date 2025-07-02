package com.health.ai_prediction_service.repository;

import com.health.ai_prediction_service.model.PredictionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionResult, Long> {
    List<PredictionResult> findByUserId(Long userId);
}
