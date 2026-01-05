package com.mealmind.backend.service;

import com.mealmind.backend.entity.User;
import com.mealmind.backend.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    public User register(String username, String password) {
        if (users.existsByUsername(username)) {
            throw new IllegalArgumentException("username already exists");
        }
        String hash = encoder.encode(password);
        return users.save(new User(username, hash));
    }

    public User authenticate(String username, String password) {
        User u = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));

        if (!encoder.matches(password, u.getPasswordHash())) {
            throw new IllegalArgumentException("invalid credentials");
        }
        return u;
    }
}
