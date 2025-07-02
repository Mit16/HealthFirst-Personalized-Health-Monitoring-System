package com.health.data.repository;

import com.health.data.model.SpO2Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SpO2Repository extends JpaRepository<SpO2Metric, Long>  {
    List<SpO2Metric> findByUserIdOrderByTimestampDesc(Long userId);
    Optional<SpO2Metric> findTopByUserIdOrderByTimestampDesc(Long userId);

    @Query("SELECT AVG(s.spo2) FROM SpO2Metric s WHERE s.userId = :userId AND DATE(s.timestamp) = :date")
    Double avgByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
