package com.sparta.icy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    private String content;

    public CommentRequestDto(String content) {
        if (content == null) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
        this.content = content;
    }
}