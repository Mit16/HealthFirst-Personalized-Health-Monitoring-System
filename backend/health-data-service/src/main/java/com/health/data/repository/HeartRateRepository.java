package com.health.data.repository;

import com.health.data.model.HeartRateMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HeartRateRepository extends JpaRepository<HeartRateMetric, Long> {
    List<HeartRateMetric> findByUserIdOrderByTimestampDesc(Long userId);
    Optional<HeartRateMetric> findTopByUserIdOrderByTimestampDesc(Long userId);
    List<HeartRateMetric> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT AVG(h.heartRate) FROM HeartRateMetric h WHERE h.userId = :userId AND DATE(h.timestamp) = :date")
    Double avgByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}

