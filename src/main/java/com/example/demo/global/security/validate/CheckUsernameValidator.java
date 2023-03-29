package com.example.demo.global.security.validate;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
@Component
public class CheckUsernameValidator extends AbstractValidator<SignUpRequestDTO> {
    private final UserRepository userRepository;
    public CheckUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    protected void doValidate(SignUpRequestDTO dto, Errors errors){
        if(userRepository.existsByUsername(dto.getUsername())){
            errors.rejectValue("username", "아이디 중복 오류", "이미 사용중인 아이디 입니다.");
        }
    }
}
