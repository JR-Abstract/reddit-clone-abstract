package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.exception.JwtTokenExpiredException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        if (authException instanceof JwtTokenExpiredException) {

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("message", authException.getMessage());

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, body.toString());

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized.");
        }
    }
}
