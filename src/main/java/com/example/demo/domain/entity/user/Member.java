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
    private String password;
    private String email;
    private String role;
    private boolean enabled;
    @Column(nullable = false)
    private int failCount;

    private boolean accountNonLocked;
    private Date lockTime;

    private Date lastLogin;
    @Builder
    public Member (Long id, String username, String password, String email, int failCount, boolean accountNonLocked, String role, boolean enabled, Date lockTime, Date lastLogin) {
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
    }
    public String getUsername() {
        return username;
    }
}