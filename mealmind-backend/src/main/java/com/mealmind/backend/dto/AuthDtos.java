package com.mealmind.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 64)
        public String username;

        @NotBlank
        @Size(min = 8, max = 128)
        public String password;
    }

    public static class LoginRequest {
        @NotBlank
        public String username;

        @NotBlank
        public String password;

        public boolean rememberMe;
    }

    public static class MeResponse {
        public Long id;
        public String username;

        public MeResponse(Long id, String username) {
            this.id = id;
            this.username = username;
        }
    }

    public static class SimpleResponse {
        public String message;

        public SimpleResponse(String message) {
            this.message = message;
        }
    }
}
