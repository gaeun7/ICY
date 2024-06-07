package com.sparta.icy.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewsfeedDto {
    private Long id;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long user_id;
}
