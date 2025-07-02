package com.health.auth.repository;

import com.health.auth.model.WearToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WearTokenRepository extends JpaRepository<WearToken, String> {
}
