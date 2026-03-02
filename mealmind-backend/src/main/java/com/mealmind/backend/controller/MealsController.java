package com.mealmind.backend.controller;

import com.mealmind.backend.security.AuthPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealsController {

    @GetMapping
    public ResponseEntity<?> list(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        AuthPrincipal p = (AuthPrincipal) auth.getPrincipal();

        List<String> demo = List.of(
                "User " + p.username() + " can see this because they are logged in",
                "Breakfast id is 1421",
                "Lunch id is 1423",
                "Dinner id is 1424",
                "Late night id is 1425"
        );

        return ResponseEntity.ok(demo);
    }
}