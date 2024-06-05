package com.sparta.icy.Dto;

import com.sparta.icy.Entity.Comment;
import lombok.Getter;

import java.util.Date;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final Date created_at;
    private final Date updated_at;
    private final Long like;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.created_at = comment.getCreated_at();
        this.updated_at = comment.getUpdated_at();
        this.like = comment.getLike();
    }
}


