package com.example.demo.global.security.service;
import com.example.demo.domain.entity.user.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/* OAuth 로 로그인 하기위해서는 OAuth2User를 구현 해줘야 한다.
*  기존에 구글링으로 찾아보며 실수한 부분은 spring security를 활용하지 않거나 UserDetails 를 사용하지 않고 로그인하는 과정이 많았는데
*  우리 프로젝트는 spring security를 사용하기 때문에 UserDetails 에 로그인 정보들을 보내줘야 제대로 인식을 한다. 그냥 값이 저장된다고 구현 되는게 아님. */

@Getter
public class CustomUserDetails extends User implements OAuth2User {
    private Member member; /* 나중에 Member 객체를 참조할 수 있도록 구현 */
    private final Long id;
    private final String email;
    private Map<String, Object> attributes;

    public CustomUserDetails(Member member) {
        super(member.getUsername(), member.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
        this.email = member.getEmail();
        this.id = member.getId();
    }
    public CustomUserDetails(Member member, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.member = member;
        this.attributes = attributes;
        this.id = member.getId();
        this.email = member.getEmail();
    }
    public CustomUserDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        /* 나중에 Member 객체를 참조할 수 있도록 구현 */
        this.member = member;
        this.id = member.getId();
        this.email = member.getEmail();
    }


    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }
    @Override
    public String getName() {
        return null;
    }
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    //계정이 만료되지 않았는지 리턴 (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겨있는지 않았는지 리턴. (true:잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return member.isAccountNonLocked();
    }

    //비밀번호가 마료되지 않았는지 리턴한다. (true:만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화(사용가능)인지 리턴 (true:활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Member getMember() {
        return member;
    }
}
