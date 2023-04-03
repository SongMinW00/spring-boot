package com.example.demo.global.security.service;
import com.example.demo.domain.entity.user.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class CustomUserDetails extends User implements OAuth2User {
    private final Member member; /* 나중에 Member 객체를 참조할 수 있도록 구현 */
    private Map<String, Object> attributes;
    public CustomUserDetails(Member member, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.member = member;
        this.attributes = attributes;
    }
    public CustomUserDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        /* 나중에 Member 객체를 참조할 수 있도록 구현 */
        this.member = member;
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
