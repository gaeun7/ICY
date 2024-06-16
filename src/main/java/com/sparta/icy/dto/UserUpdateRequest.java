package com.sparta.icy.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
public class UserUpdateRequest {
    private String nickname;
    private String intro;
    private String currentPassword;
    private String newPassword;

    public void setNickname(String nickname) {
        if (nickname == null) {
            throw new IllegalArgumentException("닉네임을 작성해주세요");
        }
        this.nickname = nickname;
    }

    public void setIntro(String intro) {
        if (intro == null) {
            throw new IllegalArgumentException("한 줄 소개를 작성해주세요");
        }
        this.intro = intro;
    }

    public void setCurrentPassword(String currentPassword) {
        if (currentPassword == null) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        if (newPassword == null) {
            throw new IllegalArgumentException("새로운 비밀번호를 입력해주세요");
        }
        this.newPassword = newPassword;
    }
}