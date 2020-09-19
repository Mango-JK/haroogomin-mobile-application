package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(mappedBy = "post")
    private List<PostHashtag> postHashtags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
     private List<Comment> comments = new ArrayList<>();

    @Column(name = "hits")
    private int hits;

    @Column(name = "post_likes")
    private int postLikes;

    @Column(name = "expired")
    private int expired;

    @Builder
    public Post(User user, String title, String content, List<PostHashtag> postHashtag, int hits, int postLikes, int expired) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.postHashtags = postHashtag;
        this.hits = hits;
        this.postLikes = postLikes;
        this.expired = expired;
    }

    public void update(String title, String content, List<PostHashtag> postHashtag) {
        this.title = title;
        this.content = content;
        this.postHashtags = postHashtag;
        this.setModifiedDate(LocalDateTime.now());
    }
}
