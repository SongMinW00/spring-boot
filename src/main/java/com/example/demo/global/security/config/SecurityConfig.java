package com.example.demo.global.security.config;

import com.example.demo.domain.service.user.CustomOAuth2UserService;
import com.example.demo.global.common.FormAuthenticationDetailsSource;
import com.example.demo.global.security.handler.CustomAccessDeniedHandler;
import com.example.demo.global.security.handler.CustomAuthenticationFailureHandler;
import com.example.demo.global.security.handler.CustomAuthenticationSuccessHandler;
import com.example.demo.global.security.provider.CustomAuthenticationProvider;
import com.example.demo.global.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.filter.CharacterEncodingFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@EnableWebSecurity // 1
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig { // 2
    @Autowired
    private FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    /* 로그인 성공 핸들러 의존성 주입 */
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    /* 로그인 실패 핸들러 의존성 주입 */
    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;
    /* OAuth 로그인을 하기위해서 CustomOAuth2UserService 의존성 주입 */
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests()
                .requestMatchers("/", "/register", "/login/**", "/base/**", "/board/**", "/sports/**", "/error/**", "/question/**", "/answer/**").permitAll()
                .requestMatchers("/content/user/**").hasRole("USER")
                .requestMatchers("/admin/config").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
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
                .and()
                .oauth2Login()          // oauth 로그인위해 Spring Security 설정
                .loginPage("/login")    // 기본 로그인 페이지를 /login 으로 설정(하지만 눈에 띄기 쉽게 메인페이지에 구현)
                .userInfoEndpoint()     // oauth 관련 정보들을 가져오는 설정
                .userService(customOAuth2UserService) // 내 데이터베이스에 넣기 위해 가공하는 과정
                .and()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }
}