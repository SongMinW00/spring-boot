package com.example.demo.Service;

import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public interface UserService {

    void joinUser(SignUpRequestDTO signUpRequestDTO);
    void logout(HttpServletRequest request, HttpServletResponse response);
    void login(String error, String exception, Model model);


//    void logindo(Authentication authentication, Model model);

//    void checkUsernameDuplication(SignUpRequestDTO signUpRequestDTO, HttpServletResponse response) throws IOException;
//    void checkEmailDuplication(SignUpRequestDTO signUpRequestDTO, HttpServletResponse response) throws IOException;

    void accessDenied(String exception, Model model);

    Map<String, String> validateHandling(Errors errors);
}
