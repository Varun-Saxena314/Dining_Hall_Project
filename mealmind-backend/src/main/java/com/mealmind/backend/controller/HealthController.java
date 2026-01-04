package com.mealmind.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "ok";
    }

    @GetMapping("/api/whaddup")
    public String yooh() {
        return "whaddup";
    }
    
}
