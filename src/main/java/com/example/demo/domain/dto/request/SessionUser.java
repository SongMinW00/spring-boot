package com.example.demo.domain.dto.request;

import com.example.demo.domain.entity.user.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private final String name;
    private final String email;

    public SessionUser(Member member) {
        this.name = member.getName();
        this.email = member.getEmail();
    }
}
