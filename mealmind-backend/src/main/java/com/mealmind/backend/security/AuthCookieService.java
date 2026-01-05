package com.mealmind.backend.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthCookieService {

    private final JwtProperties props;

    public AuthCookieService(JwtProperties props) {
        this.props = props;
    }

    public void setAuthCookie(HttpServletResponse res, String jwt, int maxAgeSeconds) {
        Cookie cookie = new Cookie(props.getCookieName(), jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        res.addCookie(cookie);
    }

    public void clearAuthCookie(HttpServletResponse res) {
        Cookie cookie = new Cookie(props.getCookieName(), "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //set to true in production. false fine for local dev
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }
}
