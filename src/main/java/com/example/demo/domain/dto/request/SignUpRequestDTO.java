package com.example.demo.domain.dto.request;

import com.example.demo.Repository.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpRequestDTO {
    private Long id;
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=\\S+$).{8,16}", message = "공백없이 비밀번호는 8~16글자 사이로 적어주세요. ")
    private String password;
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    private String role;

    public String getUsername() {
        return username;
    }
}
