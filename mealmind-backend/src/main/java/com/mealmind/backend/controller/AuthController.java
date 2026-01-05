package com.mealmind.backend.controller;

import com.mealmind.backend.dto.AuthDtos;
import com.mealmind.backend.security.AuthCookieService;
import com.mealmind.backend.security.AuthPrincipal;
import com.mealmind.backend.security.JwtService;
import com.mealmind.backend.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final int TTL_SHORT_SECONDS = 60 * 60 * 8;
    private static final int TTL_LONG_SECONDS = 60 * 60 * 24 * 30;

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthCookieService cookies;

    public AuthController(AuthService authService, JwtService jwtService, AuthCookieService cookies) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.cookies = cookies;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDtos.MeResponse> register(
            @Valid @RequestBody AuthDtos.RegisterRequest req,
            HttpServletResponse res
    ) {
        var user = authService.register(req.username, req.password);
        String jwt = jwtService.createToken(user.getId(), user.getUsername(), TTL_LONG_SECONDS);
        cookies.setAuthCookie(res, jwt, TTL_LONG_SECONDS);
        return ResponseEntity.ok(new AuthDtos.MeResponse(user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDtos.MeResponse> login(
            @Valid @RequestBody AuthDtos.LoginRequest req,
            HttpServletResponse res
    ) {
        var user = authService.authenticate(req.username, req.password);
        int ttl = req.rememberMe ? TTL_LONG_SECONDS : TTL_SHORT_SECONDS;
        String jwt = jwtService.createToken(user.getId(), user.getUsername(), ttl);
        cookies.setAuthCookie(res, jwt, ttl);
        return ResponseEntity.ok(new AuthDtos.MeResponse(user.getId(), user.getUsername()));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthDtos.SimpleResponse> logout(HttpServletResponse res) {
        cookies.clearAuthCookie(res);
        return ResponseEntity.ok(new AuthDtos.SimpleResponse("logged out"));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthDtos.MeResponse> me(Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        AuthPrincipal p = (AuthPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(new AuthDtos.MeResponse(p.userId(), p.username()));
    }
}
