package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "age_range")
    private int ageRange;

    @Column(name = "hash_tag")
    private String hashTag;

    @Column(name = "point")
    private int point;

    @Column(name = "enable_posting")
    private int enablePosting;

    @Builder
    public User(long userId, String nickname, String profileImage, String ageRange, String hashTag, int point, int enablePosting) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.ageRange = Integer.parseInt(ageRange);
        this.hashTag = hashTag;
        this.point = point;
        this.enablePosting = enablePosting;
    }

    public void update(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.setModifiedDate(LocalDateTime.now());
    }
}
