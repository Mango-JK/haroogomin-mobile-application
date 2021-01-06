package com.mango.harugomin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    private String userLoginId;
    private String password;
    private String nickname;
    private String email;
    private String profileImage;
    private int ageRange;
    private String[] userhashtags;

    @Builder
    public UserSignUpRequestDto(String userLoginId, String password, String nickname, String email, String profileImage, int ageRange, String[] userhashtags) {
        this.userLoginId = userLoginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.profileImage = profileImage;
        this.ageRange = ageRange;
        this.userhashtags = userhashtags;
    }
}
