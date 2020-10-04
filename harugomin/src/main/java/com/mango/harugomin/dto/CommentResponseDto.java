package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.Comment;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.entity.User;
import lombok.Getter;

import javax.persistence.Lob;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String nickname;
    private String profileImage;
    private Post post;
    @Lob
    private String content;
    private int commentLikes;

    public CommentResponseDto(Comment entity) {
        this.commentId = entity.getCommentId();
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.post = entity.getPost();
        this.content = entity.getContent();
        this.commentLikes = entity.getCommentLikes();
    }
}
