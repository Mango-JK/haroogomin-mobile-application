package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private long userId;
    private String nickname;
    private String profileImage;
    private int ageRange;
    private String hashTag;
    private int point;
    private int enablePosting;

    public UserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.ageRange = entity.getAgeRange();
        this.hashTag = entity.getHashTag();
        this.point = entity.getPoint();
        this.enablePosting = entity.getEnablePosting();
    }
}
