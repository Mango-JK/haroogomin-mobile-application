package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "parent_id")
    private int parentId;

    @Column(name = "sequence_num")
    private int sequenceNum;

    @Column(name = "comment_likes")
    private int commentLikes;

    @Builder
    public Comment(Post post, User user, String content, int parentId, int sequenceNum, int commentLikes) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.parentId = parentId;
        this.sequenceNum = sequenceNum;
        this.commentLikes = commentLikes;
    }

    public void update(String content) {
        this.content = content;
        this.setModifiedDate(LocalDateTime.now());
    }
}
