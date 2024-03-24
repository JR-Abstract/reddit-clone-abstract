package ua.com.javarush.oleksandr.reddit.redditcloneabstract.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ua.com.javarush.oleksandr.reddit.redditcloneabstract.handler.GlobalExceptionHandler;

import java.io.IOException;
import java.util.Map;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ResponseEntity<Map<String, Object>> responseEntity = globalExceptionHandler.handleException(authException);
        setResponseAndWriteBody(responseEntity, response, mapper);
    }

    private void setResponseAndWriteBody(ResponseEntity<Map<String, Object>> responseEntity,
            HttpServletResponse response, ObjectMapper mapper) throws IOException {

        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(responseEntity.getBody()));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
