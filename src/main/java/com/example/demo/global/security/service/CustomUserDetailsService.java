package com.example.demo.global.security.service;

import com.example.demo.domain.dao.user.UserRepository;
import com.example.demo.domain.entity.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userRepository.getMemberByUsername(username);
        Optional<Member> memberOptional = userRepository.findByUsername(username);
        if (member == null) {     /* Data Base에 SignIn 요청 이용자 ID가 존재하지 않을 경우 */
            throw new UsernameNotFoundException("해당 이용자가 존재하지 않아요 🥲");
        }

        if(memberOptional.isEmpty()) {
            throw new UsernameNotFoundException("해당 이용자가 존재하지 않아요 🥲");
        }
        if(Objects.equals(member.getUsername(), "admin")){
            Member result = memberOptional.get();
            List<GrantedAuthority> roles = new ArrayList<>();
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new CustomUserDetails(result, roles);
        }
        return new CustomUserDetails(member);
    }
}
