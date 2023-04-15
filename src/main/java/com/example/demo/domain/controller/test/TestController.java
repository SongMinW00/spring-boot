package com.example.demo.domain.controller.test;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.domain.service.file.FileService;
import com.example.demo.domain.service.user.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class TestController {

    private final FileService fileService;

    private final UserServiceImpl userService;



}
