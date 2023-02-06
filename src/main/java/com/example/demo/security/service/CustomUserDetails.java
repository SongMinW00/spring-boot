package com.example.demo.security.service;
import com.example.demo.domain.entity.user.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
public class CustomUserDetails extends User {
    private final Member member; /* 나중에 Member 객체를 참조할 수 있도록 구현 */
    public CustomUserDetails(Member member, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        /* 나중에 Member 객체를 참조할 수 있도록 구현 */
        this.member = member;
    }
    public Member getMember() {
        return member;
    }
}
