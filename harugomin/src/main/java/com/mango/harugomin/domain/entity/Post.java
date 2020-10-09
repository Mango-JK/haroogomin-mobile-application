package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "hits")
    private int hits;

    @Column(name = "comment_num")
    private int commentsNum;

    @Builder
    public Post(User user, String title, String content, String tagName, String postImage, int hits) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tagName = tagName;
        this.postImage = postImage;
        this.hits = hits;
        this.commentsNum = 0;
    }

    public void update(String title, String content, String tagName, String postImage) {
        this.title = title;
        this.content = content;
        this.tagName = tagName;
        this.postImage = postImage;
        this.setModifiedDate(LocalDateTime.now());
    }

    @Builder
    public Post(User user, String title, String content, String tagName, String postImage) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tagName = tagName;
        this.postImage = postImage;
        this.comments = new ArrayList<>();
        this.hits = 0;
        this.commentsNum = 0;
    }

    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public long getUserId() {
        return user.getUserId();
    }

    public String getUserNickname(){return user.getNickname();}

    public String getUserProfileImage(){return user.getProfileImage();}

    public void upCommentCount(){
        this.commentsNum++;
    }

}