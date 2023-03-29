package com.example.demo.global.security.validate;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
@RequiredArgsConstructor
@Component
public class CheckEmailValidator extends AbstractValidator<SignUpRequestDTO> {
    private final UserRepository userRepository;
    @Override
    protected void doValidate(SignUpRequestDTO dto, Errors errors){
        if(userRepository.existsByEmail(dto.getEmail())){
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다. ");


        }
    }
}
