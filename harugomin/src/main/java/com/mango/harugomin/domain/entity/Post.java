package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mango.harugomin.dto.PostRequestDto;
import com.mango.harugomin.service.HashtagService;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Column(name = "expired")
    private int expired;

    @Builder
    public Post(String userNickname, String title, String content, Long tagId, int hits, int postLikes, int expired) {
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.tagId = tagId;
        this.hits = hits;
        this.postLikes = postLikes;
        this.expired = expired;
    }

    public void update(String title, String content, Hashtag hashtag) {
        this.title = title;
        this.content = content;
        this.tagId = tagId;
        this.setModifiedDate(LocalDateTime.now());
    }

    @Builder
    public Post(String userNickname, String title, String content, Long tagId) {
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.tagId = tagId;
        this.hits = 0;
        this.postLikes = 0;
        this.expired = 0;
    }
}
