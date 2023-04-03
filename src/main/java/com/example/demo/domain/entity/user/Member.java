package com.example.demo.domain.entity.user;

import com.example.demo.domain.entity.basetime.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Entity // Entity 가 붙으면 JPA 가 관리, 테이블과의 매핑
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String role;
    private boolean enabled;
    @Column(nullable = false)
    private int failCount;

    private boolean accountNonLocked;
    private Date lockTime;

    private Date lastLogin;

    private String provider;
    private String providerId;


    public Member(String username, String password, String email, String role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Builder
    public Member(Long id, String username, String password, String email, int failCount, boolean accountNonLocked, String role, boolean enabled, Date lockTime, Date lastLogin, String provider, String providerId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.failCount = failCount;
        this.accountNonLocked = accountNonLocked;
        this.role = role;
        this.enabled = enabled;
        this.lockTime = lockTime;
        this.lastLogin = lastLogin;
        this.provider = provider;
        this.providerId = providerId;
    }



    public String getUsername() {
        return username;
    }

    /* 회원정보 수정을 위한 set method*/
    public void modify(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Member update(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }
}