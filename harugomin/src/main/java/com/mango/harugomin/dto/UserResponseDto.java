package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private long userId;
    private String nickname;
    private String profileImage;
    private int point;
    private int cash;

    public UserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.point = entity.getPoint();
        this.cash = entity.getCash();
    }
}
