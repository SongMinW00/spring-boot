package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServices {
    @Autowired
    private final UserRepository userRepository;

    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 30; //24hours

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void increaseFailedAttempts(Member member) {

        int newFailAttempts = member.getFailCount() + 1;
        userRepository.updateFailedAttempts(newFailAttempts, member.getUsername());


    }

    public void resetFailedAttempts(String username) {
        userRepository.updateFailedAttempts(0, username);
    }

    public void lock(Member member) {


        member = Member.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .accountNonLocked(false)
                .role("ROLE_USER")
                .failCount(member.getFailCount())
                .enabled(false)
                .lastLogin(member.getLastLogin())
                .lockTime(new Date())
                .build();

        userRepository.isLocked(member.getUsername(), member.isAccountNonLocked(), member.getLockTime());
    }

    public boolean unlockWhenTimeExpired(Member member) {

        long lockTimeInMillis = member.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            member = Member.builder()
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .email(member.getEmail())
                    .accountNonLocked(true)
                    .role("ROLE_USER")
                    .lockTime(null)
                    .failCount(0)
                    .lastLogin(member.getLastLogin())
                    .enabled(true)

                    .build();
            userRepository.unLocked(member.getUsername(), member.isAccountNonLocked(), member.getLockTime(), member.getFailCount());

            return true;
        }
        return false;
    }

    public Member getByUsername(String username) {
        return userRepository.getMemberByUsername(username);
    }
}
