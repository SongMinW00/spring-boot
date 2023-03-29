package com.example.demo.domain.service.user;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;

import com.example.demo.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Transactional

    @Override
    public void joinUser(SignUpRequestDTO signUpRequestDTO) {
        //비밀번호 암호화해서 회원가입
        Member member = Member.builder()
                .username(signUpRequestDTO.getUsername())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .email(signUpRequestDTO.getEmail())
                .accountNonLocked(true)
                .role("ROLE_USER")
                .failCount(0)
                .enabled(true)
                .lastLogin(new Date())
                .build();

        userRepository.save(member);
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* Logout 하려는 이용자 정보가 담긴 인증 객체 가져오기 */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {   /* 인증 객체가 Null이 아닌 경우 즉, 이용자가 인증한 상태인 경우 */
            /* SecurityContextLogoutHandler 이용하여 Logout 처리 */

            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }
    @Override
    public void login(String error, String exception, Model model) {
        /* model 객체에 error와 exception 내용 담아 전달 */
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

    }
    @Override
    public void accessDenied(String exception, Model model, HttpServletResponse response) throws IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            Member member = (Member) authentication.getPrincipal();
            String username = (String) authentication.getPrincipal();

            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

            model.addAttribute("username", userDetails.getMember().getUsername());
            model.addAttribute("exception", exception);
        } catch (ClassCastException e){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('접근권한이 없습니다.'); history.go(-1);</script>");
            out.flush();
        }
    }
    // 회원가입시 유효성 체크
    @Transactional // DB에 접근, 일련의 작업들을 묶어서 하나의 단위로 처리할 때
    public Map<String, String> validateHandling(Errors errors){
        // HashMap 은 key 와 value 로 구성된 Map 데이터를 해싱함수를 통해 산출된 인덱스 위치의 버킷 안에 저장하는 자료구조
        Map<String, String> validatorResult = new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            /* error.getField: 에러가 발생한 필드 이름 가져옴 */
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }
//
//    @Override
//    public Member getByUsername(String username) {
//        return userRepository.getMemberByUsername(username);
//    }
}



//    public List<UserVo> findMenbers(){
//        return repository.findAll();
//    }
//    /*회원가입 시 유효성 체크*/
//
//}