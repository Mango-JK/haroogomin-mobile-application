package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "history")
public class History extends BaseTimeEntity {
    @Id
    @Column(name = "post_id")
    private Long postId;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "post_image")
    private String postImage;

    @OneToMany(mappedBy = "post", fetch = EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "hits")
    private int hits;

    @Column(name = "comment_num")
    private int commentsNum;

    public History(Post post) {
        this.postId = post.getPostId();
        this.user = post.getUser();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tagName = post.getTagName();
        this.postImage = post.getPostImage();
        this.comments = post.getComments();
        this.hits = post.getHits();
        this.commentsNum = post.getCommentsNum();
        this.setModifiedDate(LocalDateTime.now());
    }
}
