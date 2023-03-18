package com.example.demo.controller;

import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
@RequiredArgsConstructor
@Controller
public class ErrorController {

    private final UserService userService;
    @GetMapping("/denied")
    public String accessDenied(
            @RequestParam(value = "exception", required = false) String exception,
            Model model, HttpServletResponse response) throws IOException {
        userService.accessDenied(exception, model, response);
        return "error/access_denied";
    }
}
