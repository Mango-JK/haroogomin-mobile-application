package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    @Lob
    private String title;
    @Lob
    private String content;
    private String tagName;
    private String postImage;
    private int hits;
    private int commentNum;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long userId;
    private String userNickname;
    private String userProfileImage;

    public PostResponseDto(Post entity) {
        this.postId = entity.getPostId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.tagName = entity.getTagName();
        this.postImage = entity.getPostImage();
        this.hits = entity.getHits();
        this.createdDate = entity.getCreatedDate();
        this.modifiedDate = entity.getModifiedDate();
        this.userId = entity.getUserId();
        this.userNickname = entity.getUserNickname();
        this.userProfileImage = entity.getUserProfileImage();
        this.commentNum = entity.getCommentNum();
    }
}
