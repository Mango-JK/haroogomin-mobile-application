package com.mango.harugomin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;

@Getter
@Setter
@NoArgsConstructor
public class CommentSaveRequestDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private Long postId;
    @Lob
    private String content;

    @Builder
    public CommentSaveRequestDto(Long userId, String nickname, String profileImage, Long postId, String content) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.postId = postId;
        this.content = content;
    }
}
