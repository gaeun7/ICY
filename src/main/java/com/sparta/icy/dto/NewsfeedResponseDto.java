package com.sparta.icy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NewsfeedResponseDto {
    private Long id;
    private String title;
    private int recruitmentCount;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Long user_id;

    public NewsfeedResponseDto(Long id, String title, int recruitmentCount, String content, LocalDateTime created_at, LocalDateTime updated_at, Long user_id) {
        this.id = id;
        this.title = title;
        this.recruitmentCount = recruitmentCount;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user_id = user_id;
    }
}