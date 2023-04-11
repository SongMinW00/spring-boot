package com.example.demo.domain.service.user;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    void joinUser(SignUpRequestDTO signUpRequestDTO);
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void login(String error, String exception, Model model);
    void accessDenied(String exception, Model model, HttpServletResponse response) throws IOException;
    Map<String, String> validateHandling(Errors errors);
    Member getMember(String username, String email);

    Member getMember(Long id);

    void modify(Member member);
}
