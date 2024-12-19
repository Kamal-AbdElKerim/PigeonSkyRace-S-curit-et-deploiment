package com.example.SpringSecurityDemo.Config;

import com.example.SpringSecurityDemo.Exception.shared.ErrorMessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;



import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .message("Authentication failed: Invalid email or password.")
                .timestamp(new Date())
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorMessageDTO);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .message("Access denied: You do not have permission to access this resource.")
                .timestamp(new Date())
                .code(HttpServletResponse.SC_FORBIDDEN)  // 403 Status code
                .build();

        String jsonResponse = objectMapper.writeValueAsString(errorMessageDTO);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 status code
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}
