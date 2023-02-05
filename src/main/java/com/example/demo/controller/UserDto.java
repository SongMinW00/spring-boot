//package com.example.demo.controller;
//
//import com.example.demo.Repository.Role;
//
//import lombok.*;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserDto {
//    private Long id;
//    @NotBlank(message = "아이디는 필수 입력값입니다.")
//    private String userId;
//    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
//    @Pattern(regexp = "(?=.*[a-zA-Z])(?=\\S+$).{8,16}", message = "공백없이 비밀번호는 8~16글자 사이로 적어주세요. ")
//    private String password;
//
//    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식을 맞춰주세요.")
//    @NotBlank(message = "이메일은 필수 입력값입니다.")
//    private String userEmail;
//
//    private Role role;
//
//
//
//}
