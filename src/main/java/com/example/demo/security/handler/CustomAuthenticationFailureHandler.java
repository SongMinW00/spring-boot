package com.example.demo.security.handler;

import com.example.demo.Service.UserServices;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserServices userServices;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해 주세요. ";
        String username = request.getParameter("username");
        Member member = userServices.getByUsername(username);
        if(member != null){
            if(member.isEnabled() == member.isAccountNonLocked()){
                if(member.getFailCount() < UserServices.MAX_FAILED_ATTEMPTS - 1) {
                    userServices.increaseFailedAttempts(member);
                } else {
                    userServices.lock(member);
                    System.out.println("계정잠김");
                    exception = new LockedException("계정잠김");
                }
            } else if(!member.isAccountNonLocked()){
                if(userServices.unlockWhenTimeExpired(member)){
                    exception = new LockedException("계정잠김");
                }
                exception = new LockedException("계정잠김");
            }
        }



         if(exception instanceof BadCredentialsException){

        }
        else if (exception instanceof LockedException) {  /* 인증 요청자가 비밀번호를 틀렸을 경우 */
            errorMessage = "계정이 잠겼습니다. 24시간 이후에 다시 로그인 해주세요 🤪";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요. 🥵";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요. 😱";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요. 🤬";
        } else if (exception instanceof InsufficientAuthenticationException) {  /* 인증 요청자가 부가적으로 인증 시 제공할 값이 틀렸을 경우 */
            errorMessage = "Please check the additional verification value.";
        }
        else {
            errorMessage = "알수없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요. 🤫";
        }


        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
        /* 부모 Class(SimpleUrlAuthenticationFailureHandler)에게 인증 실패 작업 위임 */
        super.onAuthenticationFailure(request, response, exception);
    }

}
