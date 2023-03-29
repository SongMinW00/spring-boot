package com.example.demo.global.security.config;

import com.example.demo.global.common.FormAuthenticationDetailsSource;
import com.example.demo.global.security.handler.CustomAccessDeniedHandler;
import com.example.demo.global.security.handler.CustomAuthenticationFailureHandler;
import com.example.demo.global.security.handler.CustomAuthenticationSuccessHandler;
import com.example.demo.global.security.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CharacterEncodingFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@EnableWebSecurity // 1
@Configuration
@RequiredArgsConstructor
public class SecurityConfig { // 2
    @Autowired
    private FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    /* 로그인 성공 핸들러 의존성 주입 */
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    /* 로그인 실패 핸들러 의존성 주입 */
    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    /* 시큐리티가 로그인 과정에서 password를 가로챌때 어떤 해쉬로 암호화 했는지 확인 */
    public PasswordEncoder passwordEncoder() {     /* Password Hash 암호화 기능 사용을 위한 객체 주입 */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        /* 로그인 인증수행시 권한과 아이디 비밀번호등 일치하는지 검사 */
//        auth.authenticationProvider(authenticationProvider());
//    }
    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/css/**", "/js/**", "/assets/**", "/lib/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeHttpRequests()
                .requestMatchers("/", "/register", "/login/**", "/base/**", "/board/**", "/sports/**", "/error/**", "/question/**").permitAll()
                .requestMatchers("/content/user/**").hasRole("USER")
                .requestMatchers("/admin/config").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .expiredUrl("/login?expire=true")
                .and()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/login_proc")
                .authenticationDetailsSource(formAuthenticationDetailsSource)
                .defaultSuccessUrl("/")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        HttpSession session = request.getSession();
                        session.invalidate();

                    }
                }).logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                        response.sendRedirect("content/base/login");
                    }
                }).deleteCookies("remember-me", "JSESSIONID")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }
}