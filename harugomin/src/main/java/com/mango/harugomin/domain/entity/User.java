package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

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

    @Builder
    public User(long userId, String nickname, String profileImage, int ageRange, List<UserHashtag> userHashtags) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.ageRange = ageRange;
        this.userHashtags = userHashtags;
    }

    @Builder
    public User(long userId, int ageRange) {
        this.userId = userId;
        this.ageRange = ageRange;
    }

    public void update(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.setModifiedDate(LocalDateTime.now());
    }


    public void updateUserImage(String imagePath) {
        this.profileImage = imagePath;
        this.setModifiedDate(LocalDateTime.now());
    }

    public void updateUserProfile(UserUpdateRequestDto updateRequestDto) {
        this.ageRange = updateRequestDto.getAgeRange();
        this.nickname = updateRequestDto.getNickname();
        this.ageRange = updateRequestDto.getAgeRange();
        this.profileImage = updateRequestDto.getProfileImage();
        this.setModifiedDate(LocalDateTime.now());
    }

    public void initHashtag(){
        this.userHashtags = new ArrayList<>();
        this.setModifiedDate(LocalDateTime.now());
    }

    public void userAddTag(UserHashtag userHashtag) {
        this.userHashtags.add(userHashtag);
        this.setModifiedDate(LocalDateTime.now());
    }

}
