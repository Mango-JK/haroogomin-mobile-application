package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "post")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "post_hashtag")
    private String postHashtag;

    // private List<Comment> comments = new ArrayList<>();

    @Column(name = "hits")
    private int hits;

    @Column(name = "likes")
    private int likes;

    @Column(name = "expired")
    private int expired;

    @Builder
    public Post(User user, String title, String content, String postHashtag, int hits, int likes, int expired) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.postHashtag = postHashtag;
        this.hits = hits;
        this.likes = likes;
        this.expired = expired;
    }
}
