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

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    private int point;

    private int cash;



    @Builder
    public User(long userId, String nickname, String profileImage, int point, int cash) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.point = point;
        this.cash = cash;
    }

    public void update(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.setModifiedDate(LocalDateTime.now());
    }
}