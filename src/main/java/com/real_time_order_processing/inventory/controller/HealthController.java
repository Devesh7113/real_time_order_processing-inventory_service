package com.real_time_order_processing.inventory.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class HealthController
{
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health(HttpServletRequest request)
    {
        log.info("Inventory /health called from {} {}", request.getRemoteAddr(), request.getMethod());
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
