package com.example.demo.Repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum Role {
    ADMIN("ROLE_ADMIN", "어드민"),
    MEMBER("ROLE_MEMBER", "사용자");

    private final String key;
    private final String title;
}
