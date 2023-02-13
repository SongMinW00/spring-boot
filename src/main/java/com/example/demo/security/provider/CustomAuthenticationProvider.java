package com.example.demo.security.provider;
import com.example.demo.security.common.FormWebAuthenticationDetails;
import com.example.demo.security.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {      /* Password 및 추가 검증을 위한 Logic 처리 */
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getMember().getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸어요 🥲");
        }
        /* 이용자가 Login을 할 때, Secret Key라는 값을 함께 전달한다는 가정하에 해당 Key를 꺼내기 위한 객체 생성*/
        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails) authentication.getDetails();
        /* SecretKey를 빼서 문자열 객체로 저장 (OTP 구현할 때, 용이) */
        String secretKey = details.getSecretKey();
        if (secretKey == null || !"secret".equals(secretKey)) {     /* 인증 요청 이용자가 보낸 값 중 secretkey가 없거나, 해당 내용이 secret이 아닐 경우 */
            throw new InsufficientAuthenticationException("유효하지 않은 Secret Key가 전달 되었습니다!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getMember(), null, userDetails.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {  /* authentication 객체 Type과 CustomAuthenticationProvider에서 이용하고자 하는 Token 값이 일치하는지 확인하고, 인증 처리를 할 수 있도록 구현 */
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
