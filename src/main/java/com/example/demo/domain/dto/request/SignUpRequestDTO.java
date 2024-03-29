package com.example.demo.domain.dto.request;

import jakarta.persistence.Column;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

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

    private int failCount;
    private Date lockTime;
    private Date latsLogin;
    private boolean accountNonLocked;
    private boolean enabled;
    public boolean isPwEqualToCheckPw(){
        return password.equals(password2);
    }
    public String getUsername() {
        return username;
    }
}

