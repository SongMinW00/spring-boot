package com.example.demo.domain.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data   // Getter, Setter, ToString, EqualsAndHashCode와 @RequiredArgsConstructor
        // 를 합쳐놓은거
public class SignUpRequestDTO {
    private Long id;
    /* 형식 설정을 위해 @NotBlank, @Pattern 추가 */
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=\\S+$).{8,16}", message = "공백없이 비밀번호는 8~16글자 사이로 적어주세요. ")
    private String password;
    @NotBlank(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    private String role;

    public boolean isPwEqualToCheckPw(){
        return password.equals(password2);
    }
    public String getUsername() {
        return username;
    }
}

