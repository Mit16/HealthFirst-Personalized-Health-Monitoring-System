package com.health.data.repository;

import com.health.data.model.AccelerometerLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccelerometerLogRepository extends JpaRepository<AccelerometerLog, Long> {
    List<AccelerometerLog> findByUserIdOrderByTimestampDesc(Long userId);
}
