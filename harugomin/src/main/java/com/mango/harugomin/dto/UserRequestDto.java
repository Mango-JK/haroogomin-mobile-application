package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.User;
import lombok.Getter;

@Getter
public class UserRequestDto {
    private long userId;
    private String nickname;
    private String profileImage;
    private int ageRange;

    public UserRequestDto(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.ageRange = entity.getAgeRange();
    }
}
