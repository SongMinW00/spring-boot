package com.example.demo.global.common;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;

@Getter
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {
    private String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request){ /* 인증 요청 이용자가 전달하는 추가 Parameter 값 저장 */
        super(request);
        secretKey = request.getParameter("secret_key");
    }
}
