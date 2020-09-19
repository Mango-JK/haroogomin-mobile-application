package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.dto.UserUpdateResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.FetchType.*;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = EAGER)
    private List<UserHashtag> userHashtags = new ArrayList<>();

    @Column(name = "point")
    private int point;

    @Column(name = "enable_posting")
    private int enablePosting;

    @Builder
    public User(long userId, String nickname, String profileImage, int ageRange, int point, int enablePosting) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.ageRange = ageRange;
        this.point = point;
        this.enablePosting = enablePosting;
    }

    public void update(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.setModifiedDate(LocalDateTime.now());
    }

    public void updateProfileImage(String imagePath) {
        this.profileImage = imagePath;
        this.setModifiedDate(LocalDateTime.now());
    }

    public void updateProfile(UserUpdateRequestDto updateRequestDto) {
        this.nickname = updateRequestDto.getNickname();
        this.profileImage = updateRequestDto.getProfileImage();
        this.ageRange = updateRequestDto.getAgeRange();
        this.setModifiedDate(LocalDateTime.now());
    }

    public void addHashtag(UserHashtag userHashtag) {
        this.userHashtags.add(userHashtag);
        this.setModifiedDate(LocalDateTime.now());
    }

    public void initHashtag(){
        this.userHashtags = new ArrayList<>();
        this.setModifiedDate(LocalDateTime.now());
    }

}
