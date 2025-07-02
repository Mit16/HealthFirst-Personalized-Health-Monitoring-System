package com.health.data.repository;

import com.health.data.model.DailyStepMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StepRepository extends JpaRepository<DailyStepMetric, Long>  {
    List<DailyStepMetric> findByUserIdOrderByTimestampDesc(Long userId);
    Optional<DailyStepMetric> findTopByUserIdOrderByTimestampDesc(Long userId);

    @Query("SELECT SUM(d.steps) FROM DailyStepMetric d WHERE d.userId = :userId AND DATE(d.timestamp) = :date")
    Integer findStepsByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

}
