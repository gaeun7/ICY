package com.sparta.icy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsfeedDto {
    private String title;
    private int recruitmentCount;
    private String content;

    public NewsfeedDto(String title, int recruitmentCount, String content) {
        this.title = title;
        this.recruitmentCount = recruitmentCount;
        this.content = content;
    }
}