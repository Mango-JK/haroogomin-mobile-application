package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "comment_likes")
    private int commentLikes;

    @Column(name = "is_like")
    private boolean isLike = false;

    @Builder
    public Comment(Long userId, String nickname, String profileImage, Post post, String content, int commentLikes) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.post = post;
        this.content = content;
        this.commentLikes = commentLikes;
        this.isLike = false;
    }

    public void update(String content) {
        this.content = content;
        this.setModifiedDate(LocalDateTime.now());
    }

    public void userLikeThis(){
        this.isLike = true;
    }

}
