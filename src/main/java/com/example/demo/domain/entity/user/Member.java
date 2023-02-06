package com.example.demo.domain.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity // Entity 가 붙으면 JPA 가 관리, 테이블과의 매핑
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String password2;
    private String email;
    private String role;

    @Builder
    public Member (Long id, String username, String password, String password2, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.email = email;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
}