package com.example.demo.global.common;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
@Component  // Bean Configuration 파일에 Bean 을 따로 등록하지 않아도 사용가능
public class FormAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context){
        return new FormWebAuthenticationDetails(context);
    }
}
