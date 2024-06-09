package com.sparta.icy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @NotBlank
    @Size(min = 10, max = 20, message = "사용자 ID는 10글자 이상 20글자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "사용자 ID는 대소문자 영문자와 숫자만 포함해야 합니다.")
    private String username;
    @NotBlank
    @Size(min = 10, message = "비밀번호는 최소 10글자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}$",
            message = "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    private String intro;
    @Email
    @NotBlank
    private String email;
    private boolean isStatus=true;

}