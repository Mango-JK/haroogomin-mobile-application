package com.mango.harugomin.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserUpdateRequestDto {
    private long userId;
    private String nickname;
    private String profileImage;
    private int ageRange;
    private String[] userHashtags;

    @Builder
    public UserUpdateRequestDto(Long userId, String nickname, String profileImage, int ageRange, String[] userHashtags) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.ageRange = ageRange;
        this.userHashtags = userHashtags;
    }
}