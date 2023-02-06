package com.example.demo.security.handler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();      // 이용자가 현재 요청을 보내기 전 거쳐왔던 정보를 담은 객체
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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