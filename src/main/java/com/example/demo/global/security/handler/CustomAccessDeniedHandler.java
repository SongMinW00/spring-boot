package com.example.demo.global.security.handler;

import lombok.Setter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Setter
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage;
    @Override
    public  void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String deniedURI = errorPage + "?exception=" + accessDeniedException.getMessage();
        response.sendRedirect(deniedURI);
    }
}
