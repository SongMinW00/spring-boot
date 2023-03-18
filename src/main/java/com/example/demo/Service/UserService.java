package com.example.demo.Service;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
public interface UserService {
    void joinUser(SignUpRequestDTO signUpRequestDTO);
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void login(String error, String exception, Model model);
    void accessDenied(String exception, Model model, HttpServletResponse response) throws IOException;
    Map<String, String> validateHandling(Errors errors);
}
