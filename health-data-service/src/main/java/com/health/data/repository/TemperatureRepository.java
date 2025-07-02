package com.health.data.repository;

import com.health.data.model.TemperatureMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TemperatureRepository extends JpaRepository<TemperatureMetric, Long> {
    List<TemperatureMetric> findByUserIdOrderByTimestampDesc(Long userId);

    Optional<TemperatureMetric> findTopByUserIdOrderByTimestampDesc(Long userId);

    @Query("SELECT AVG(t.bodyTemperature) FROM TemperatureMetric t WHERE t.userId = :userId AND DATE(t.timestamp) = :date")
    Double avgByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

}
