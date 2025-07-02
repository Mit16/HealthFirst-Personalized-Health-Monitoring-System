package com.health.data.repository;

import com.health.data.model.SleepSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SleepSessionRepository extends JpaRepository<SleepSession, Long>  {
    List<SleepSession> findByUserIdOrderByTimestampDesc(Long userId);
    Optional<SleepSession> findTopByUserIdOrderByTimestampDesc(Long userId);

//    @Query("SELECT s.sleepHours FROM SleepSession s WHERE s.userId = :userId AND FUNCTION('DATE', s.sleepStart) = :date")
//    Integer findSleepDurationByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT SUM(s.sleepHours) FROM SleepSession s WHERE s.userId = :userId AND FUNCTION('DATE', s.sleepStart) = :date")
    Integer findSleepDurationByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);



}
