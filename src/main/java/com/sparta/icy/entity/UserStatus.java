package com.sparta.icy.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    IN_ACTION("정상"),
    SECESSION("탈퇴");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }
}


