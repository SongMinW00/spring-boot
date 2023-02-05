package com.example.demo.Service.implement;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.UserService;

import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void joinUser(SignUpRequestDTO signUpRequestDTO) {
        //비밀번호 암호화
        Member member = Member.builder()
                .username(signUpRequestDTO.getUsername())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .email(signUpRequestDTO.getEmail())
                .role(signUpRequestDTO.getRole())
                .build();
        userRepository.save(member);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {

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
    public void accessDenied(String exception, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Member member = (Member) authentication.getPrincipal();

        model.addAttribute("username", member.getUsername());
        model.addAttribute("exception", exception);
    }

    // 회원가입시 유효성 체크
    @Transactional
    public Map<String, String> validateHandling(Errors errors){
        Map<String, String> validatorResult = new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

//    public  void checkUsernameDuplication(SignUpRequestDTO signUpRequestDTO, HttpServletResponse response) throws IOException {
//        boolean usernameDuplicate = userRepository.existsByUsername(signUpRequestDTO.getUsername());
//        if(usernameDuplicate){
//            response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println("<script>alert('아이디가 중복되었으니 다시 입력해주시기 바랍니다. '); history.go(-1);</script>");
//            out.flush();
//
//        }
//
//    }

//    public  void checkEmailDuplication(SignUpRequestDTO signUpRequestDTO, HttpServletResponse response) throws IOException {
//        boolean emailDuplicate = userRepository.existsByEmail(signUpRequestDTO.getEmail());
//        if(emailDuplicate){
//            response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.println("<script>alert('이메일이 중복되었으니 다시 입력해주시기 바랍니다. '); history.go(-1);</script>");
//            out.flush();
//        }
//    }
}



//    private void validateDuplicateUser(UserDto userDto){
//        Optional<UserVo> findUserId = userRepository.findByUserId(userDto.getUserId());
//        if(findUserId.isPresent()){
//            throw new UserIdDuplicateException("아이디가 중복되었습니다. 다시 설정해주세요.", ErrorCode.USERID_DUPLICATION);
//        }
//    }


    /* 아이디, 닉네임, 이메일 중복 여부 확인 */
//    public boolean checkUserIdDuplicate(String userId){
//        return userRepository.existsByUserId(userId);
//    }



//    public List<UserVo> findMenbers(){
//        return repository.findAll();
//    }
//    /*회원가입 시 유효성 체크*/
//
//}