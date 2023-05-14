package com.codestates.member.entity;

import lombok.Getter;

public enum Role {

    USER("ROLE_USER"),

    ADMIN("ROLE_ADMIN");

    @Getter
    private String role;

    Role(String role) {
        this.role = role;
    }
}
