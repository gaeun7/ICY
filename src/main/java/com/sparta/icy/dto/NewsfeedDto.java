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
}