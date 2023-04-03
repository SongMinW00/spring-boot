package com.example.demo.domain.service.user;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.entity.oauth.OAuthAttributes;
import com.example.demo.domain.entity.user.Member;
import com.example.demo.global.security.service.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
@Lazy
public class CustomOAuth2UserService extends DefaultOAuth2UserService {



    @Autowired
    private UserRepository userRepository;




    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        /* OAuth2 서비스 id 구분코드 ( 구글, 카카오, 네이버 ) */
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = "google"+uuid;
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        /* OAuth2UserService */
        Member userEntity = userRepository.getMemberByUsername(username);

        if(userEntity == null){
            userEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new CustomUserDetails(userEntity, oAuth2User.getAttributes(), roles);
    }
    @Transactional
        private Member saveOrUpdate(OAuthAttributes attributes){
            Member member = userRepository.findByEmail(attributes.getEmail()).map(entity->entity.update(attributes.getName(), attributes.getEmail()))
                    .orElse(attributes.toEntity());

            return userRepository.save(member);
        }

}