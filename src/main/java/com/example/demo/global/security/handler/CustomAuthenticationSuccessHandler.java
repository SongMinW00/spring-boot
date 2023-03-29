package com.example.demo.global.security.handler;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.entity.user.Member;

import com.example.demo.domain.service.user.UserServices;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServices userServices;
    private RequestCache requestCache = new HttpSessionRequestCache();      // 이용자가 현재 요청을 보내기 전 거쳐왔던 정보를 담은 객체
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Optional<Member> member = userRepository.findByUsername(authentication.getName());
        if(!member.get().isAccountNonLocked()){
            throw new LockedException("계정 잠겨있음");
        }
        member.ifPresent(value -> value.setLastLogin(new Date()));
        if(member.get().getFailCount() > 0){
            userServices.resetFailedAttempts(member.get().getUsername());
        }
        setDefaultTargetUrl("/");       /* 인증 요청 이용자가 이 전에 요청한 정보가 없다면 root page로 이동시키기 위해 해당 URI 문자열 값 전달 */
        /* 인증 요청 이용자가 인증 요청을 보내기 전에 요청했던 정보를 담고 있는 객체 생성 */
        SavedRequest saveRequest = requestCache.getRequest(request, response);
        if (saveRequest != null) {      /* 인증 요청 이용자가 인증 요청을 보내기 전에 요청했던 정보가 Null이 아니라면? 즉, 어떤 요청을 보낸적이 있다면? */
            /* 해당 이용자가 이 전에 요청을 보냈던 URI 정보를 꺼내서 문자열 Type으로 저장 */
            String redirectUrl = saveRequest.getRedirectUrl();
            /* 해당 이용자가 이 전에 요청을 보낸 URI로 이동하게 처리 */
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        } else {
            /* 해당 이용자가 이 전에 보냈던 요청 정보가 없으므로, root page(24번째 줄 참조)로 이동하게 처리 */
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}