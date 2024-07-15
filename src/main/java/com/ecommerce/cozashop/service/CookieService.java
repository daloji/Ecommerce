package com.ecommerce.cozashop.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class CookieService {
    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    public Cookie create(String name, String value, int days) {
        String encodeValue = Base64.getEncoder().encodeToString(value.getBytes());
        Cookie cookie = new Cookie(name, encodeValue);

        cookie.setMaxAge(days * 24 * 60 * 60); // In seconds
        cookie.setPath("/");
        response.addCookie(cookie);

        return cookie;
    }

    public Cookie create(String name, String value, int days, HttpServletResponse resp) {
        String encodeValue = Base64.getEncoder().encodeToString(value.getBytes());
        Cookie cookie = new Cookie(name, encodeValue);

        cookie.setMaxAge(days * 24 * 60 * 60); // In seconds
        cookie.setPath("/");
        resp.addCookie(cookie);

        return cookie;
    }

    public Cookie readCookie(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase(name)) {
                    String decodeValue = new String(Base64.getDecoder().decode(c.getValue()));
                    c.setValue(decodeValue);
                    return c;
                }
            }
        }
        return null;
    }

    public Cookie readCookie(String name, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase(name)) {
                    String decodeValue = new String(Base64.getDecoder().decode(c.getValue()));
                    c.setValue(decodeValue);
                    return c;
                }
            }
        }
        return null;
    }

    public void deleteCookie(String name) {
        this.create(name, "", 0);
    }
}
