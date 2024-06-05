package com.sparta.icy.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequestDto {
    private String content;
    private Date created_at;
    private Date updated_at;
    private Long like;
}
