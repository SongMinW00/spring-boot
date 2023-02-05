package com.example.demo.security.handler;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해 주세요. ";

        if (exception instanceof BadCredentialsException) {  /* 인증 요청자가 비밀번호를 틀렸을 경우 */
            errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다. 다시 시도해 주세요. ";
        } else if(exception instanceof UsernameNotFoundException){
            errorMessage = "계정이 존재하지 않습니다. 회원가입 진행 후 로그인 해주세요. ";
        }
        else if(exception instanceof InternalAuthenticationServiceException){
            errorMessage = "내부적으로 발생한 시스템 문제로 인해 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
        }
        else if(exception instanceof AuthenticationCredentialsNotFoundException){
            errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요. ";
        }
        else if (exception instanceof InsufficientAuthenticationException) {  /* 인증 요청자가 부가적으로 인증 시 제공할 값이 틀렸을 경우 */
            errorMessage  = "Please check the additional verification value.";
        }
//        else if(exception instanceof UserIdDuplicateException){
//            errorMessage = "이미 존재하는 아이디입니다. ";
//        }
        else{
            errorMessage = "알수없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요. ";
        }
        /* 인증 실패 시 이동할 경로와 Error Message가 보이게 처리 */
//        response.setContentType("text/html; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.println("<script>alert('아이디나 패스워드가 틀렸습니다. 다시 입력해주세요.'); history.go(-1);</script>");
//        out.flush();
        //UTF-8 인코딩 처리
        errorMessage = URLEncoder.encode(errorMessage,"UTF-8");
        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
        /* 부모 Class(SimpleUrlAuthenticationFailureHandler)에게 인증 실패 작업 위임 */
        super.onAuthenticationFailure(request, response, exception);
    }
}
