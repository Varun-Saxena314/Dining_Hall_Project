package com.mealmind.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

//DTOs carry object data between networks or between layers. It converts JSONs to objects that allow your program to "speak"

public class AuthDtos { //main class
//first two classes for incoming DTOs. JSON -> DTO
    public static class RegisterRequest { //JSON class for Regsiter Request
        @NotBlank //JSON username cannot be blank
        @Size(min = 3, max = 64) //must be between 3 and 64
        public String username; //feature username for RegisterRequest

        @NotBlank
        @Size(min = 8, max = 128)
        public String password;
    }

    public static class LoginRequest {//JSON login request class
        @NotBlank
        public String username; //Username cannot be blank

        @NotBlank
        public String password; //Password cannot be blank

        public boolean rememberMe; //Can have a rememberMe to avoid repeat login
    }
//last two are outgoing DTOs. DTO -> JSON
    public static class MeResponse { //JSON that returns to client
        public Long id; 
        public String username; //JSON will include id of user and username

        public MeResponse(Long id, String username) {
            this.id = id;
            this.username = username; //creates DTO
        }
    }

    public static class SimpleResponse { //Simple messages returned
        public String message; //message in json

        public SimpleResponse(String message) {
            this.message = message; //creates DTO message
        }
    }
}
