package com.example.placement_portal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "Placement Portal API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "College Placement Portal");
        info.put("description", "AI-powered placement management system");
        info.put("features", new String[]{
            "Resume Processing with AI",
            "Automatic Student Shortlisting",
            "Company-Student Matching",
            "Placement Analytics",
            "Role-based Access Control"
        });
        
        return ResponseEntity.ok(info);
    }
}