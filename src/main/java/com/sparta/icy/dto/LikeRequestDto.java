package com.sparta.icy.dto;

import com.sparta.icy.entity.ContentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeRequestDto {
    private Long content_id;
    private boolean likebtn;
    private ContentType contentType;
}
