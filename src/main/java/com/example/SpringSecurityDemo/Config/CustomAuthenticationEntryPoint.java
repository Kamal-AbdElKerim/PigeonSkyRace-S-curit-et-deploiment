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
        // Log the error

        // Customize the error message for different authentication errors
        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .message("Authentication failed: Invalid credentials.Please check your email and password, and try again.")
                .timestamp(new Date())
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .build();

        // Handle specific authentication exceptions (like JWT expiration)
//        if (authException instanceof JwtExpiredException) {
//            errorMessageDTO = ErrorMessageDTO.builder()
//                    .message("Authentication failed: JWT token expired.")
//                    .timestamp(new Date())
//                    .code(HttpServletResponse.SC_UNAUTHORIZED)
//                    .build();
//        }

        // Send the error response
        String jsonResponse = objectMapper.writeValueAsString(errorMessageDTO);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        // Log the access denied event

        // Customize the error message for access denial
        String message = "Access denied: You do not have permission to access this resource.";
        if (request.getRequestURI().contains("/admin")) {
            message = "Access denied: Admin role is required.";
        }

        ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .message(message)
                .timestamp(new Date())
                .code(HttpServletResponse.SC_FORBIDDEN)
                .build();

        // Send the error response
        String jsonResponse = objectMapper.writeValueAsString(errorMessageDTO);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}

