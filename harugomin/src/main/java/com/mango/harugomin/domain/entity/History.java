package com.mango.harugomin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class History extends BaseTimeEntity{
    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    private Long tagId;

    @OneToMany(mappedBy = "post", fetch = EAGER)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "hits")
    private int hits;

    @Column(name = "post_likes")
    private int postLikes;

    public History(Post post) {
        this.postId = post.getPostId();
        this.userNickname = post.getUserNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tagId = post.getTagId();
        this.comments = post.getComments();
        this.hits = post.getHits();
        this.postLikes = post.getPostLikes();
    }
}
