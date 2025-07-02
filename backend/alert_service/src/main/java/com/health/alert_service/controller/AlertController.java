package com.health.alert_service.controller;

import com.health.alert_service.dto.AlertTriggerDTO;
import com.health.alert_service.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.health.alert_service.model.Alert;
import java.util.List;


@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService service;

    @PostMapping("/trigger")
    public ResponseEntity<String> trigger(@RequestBody AlertTriggerDTO dto) {
        service.triggerAlert(dto);
        return ResponseEntity.ok("Alert processed");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Alert>> getUserAlerts(@RequestHeader("Authorization") String token,
                                                     @PathVariable Long userId) {
        return ResponseEntity.ok(service.getAlertsByUserId(userId));
    }


}
