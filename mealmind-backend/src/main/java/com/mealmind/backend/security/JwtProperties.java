package com.mealmind.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProperties {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.cookieName}")
    private String cookieName;

    public String getSecret() { return secret; }
    public String getIssuer() { return issuer; }
    public String getCookieName() { return cookieName; }
}
