package com.mango.harugomin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String userLoginId;
    private String password;
    private String nickname;
    private String profileImage;
    private int ageRange;
    private String[] userhashtags;

    @Builder
    public UserSignUpRequestDto(String userLoginId, String password, String nickname, String profileImage, int ageRange, String[] userhashtags) {
        this.userLoginId = userLoginId;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.ageRange = ageRange;
        this.userhashtags = userhashtags;
    }
}
