package com.example.demo.security.Config;
import com.example.demo.constant.ServiceURIManagement;
import com.example.demo.security.common.FormAuthenticationDetailsSource;
import com.example.demo.security.handler.CustomAccessDeniedHandler;
import com.example.demo.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@EnableWebSecurity // 1
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{ // 2
    @Autowired private FormAuthenticationDetailsSource formAuthenticationDetailsSource;
    /* 로그인 성공 핸들러 의존성 주입 */
    @Autowired private AuthenticationSuccessHandler authenticationSuccessHandler;
    /* 로그인 실패 핸들러 의존성 주입 */
    @Autowired private AuthenticationFailureHandler authenticationFailureHandler;
    @Bean
    /* 시큐리티가 로그인 과정에서 password를 가로챌때 어떤 해쉬로 암호화 했는지 확인 */
    public PasswordEncoder passwordEncoder() {     /* Password Hash 암호화 기능 사용을 위한 객체 주입 */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        return new CustomAuthenticationProvider();
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* 로그인 인증수행시 권한과 아이디 비밀번호등 일치하는지 검사 */
        auth.authenticationProvider(authenticationProvider());
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        /* 보안검사 필요없는거 여기다 추가 */
        web.ignoring().antMatchers("/css/**","/js/**","/assets/**", "/lib/**","/template/content/**");
    }
    @Override
    public void configure(final HttpSecurity http) throws Exception{
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
                .authorizeRequests()
                .antMatchers("/","/register", "/login*", "/login/**").permitAll()
                .antMatchers(ServiceURIManagement.NOW_VERSION + "/user/**").hasRole("USER")
                .antMatchers(ServiceURIManagement.NOW_VERSION + "/manager/**").hasRole("MANAGER")
                .antMatchers(ServiceURIManagement.NOW_VERSION + "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
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

                        response.sendRedirect("content/login");
                    }
                }).deleteCookies("remember-me")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }


}