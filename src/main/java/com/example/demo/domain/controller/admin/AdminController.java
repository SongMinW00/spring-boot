package com.example.demo.domain.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@RequiredArgsConstructor
@Controller
public class AdminController {
    @GetMapping("/admin/config")
    public String adminConfig() {
        return "content/admin/config";
    }
}
