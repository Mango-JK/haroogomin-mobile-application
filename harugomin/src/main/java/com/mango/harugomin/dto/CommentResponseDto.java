package com.mango.harugomin.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mango.harugomin.domain.entity.BaseTimeEntity;
import com.mango.harugomin.domain.entity.Comment;
import com.mango.harugomin.domain.entity.Post;
import lombok.Getter;

import javax.persistence.Lob;

@Getter
public class CommentResponseDto extends BaseTimeEntity {
    private Long commentId;
    private Long userId;
    private String nickname;
    private String profileImage;
    @JsonIgnore
    private Post post;
    @Lob
    private String content;
    private int commentLikes;
    private boolean isLike;

    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getCommentId();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.post = entity.getPost();
        this.content = entity.getContent();
        this.commentLikes = entity.getCommentLikes();
        this.setCreatedDate(entity.getCreatedDate());
        this.setModifiedDate(entity.getModifiedDate());
        this.isLike = false;
    }
}
