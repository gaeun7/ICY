package com.sparta.icy.dto;

import com.sparta.icy.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    // private final Long like;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreated_at();
        this.updatedAt = comment.getUpdated_at();
        // this.like = comment.getLike();
    }
}
