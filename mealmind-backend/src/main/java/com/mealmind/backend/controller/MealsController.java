package com.mealmind.backend.controller;

import com.mealmind.backend.dto.MenuDtos;
import com.mealmind.backend.service.CampusDishService;
import com.mealmind.backend.service.MenuParseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meals")
public class MealsController {

    private final CampusDishService campusDishService;
    private final MenuParseService menuParseService;

    public MealsController(CampusDishService campusDishService, MenuParseService menuParseService) {
        this.campusDishService = campusDishService;
        this.menuParseService = menuParseService;
    }

    @GetMapping
    public ResponseEntity<MenuDtos.MenuResponse> getMeals(
            @RequestParam String periodId,
            @RequestParam String date
    ) {
        String json = campusDishService.fetchMenuJson(periodId, date);
        MenuDtos.MenuResponse parsed = menuParseService.parseMenu(json);
        return ResponseEntity.ok(parsed);
    }
}