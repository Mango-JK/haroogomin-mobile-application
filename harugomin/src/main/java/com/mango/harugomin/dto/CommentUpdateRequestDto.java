package com.mango.harugomin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Lob;

@Setter
@Getter
@AllArgsConstructor
public class CommentUpdateRequestDto {
    private Long postId;
    @Lob
    private String content;
}
