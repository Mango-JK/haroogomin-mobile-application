package com.mango.harugomin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@NoArgsConstructor
@Getter
public class PostSaveRequestDto {
    private Long postId;
    private Long userId;
    private String title;
    @Lob
    private String content;
    private String tagName;
    private String postImage;

    @Builder
    public PostSaveRequestDto(Long postId, Long userId, String title, String content, String tagName, String postImage) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tagName = tagName;
        this.postImage = postImage;
    }
}
