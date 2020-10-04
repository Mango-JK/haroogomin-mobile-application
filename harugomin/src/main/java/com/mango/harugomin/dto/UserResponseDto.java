package com.mango.harugomin.dto;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.entity.UserHashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private int ageRange;
    private List<UserHashtag> userHashtags = new ArrayList<>();

    public UserResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.ageRange = entity.getAgeRange();
        this.userHashtags = entity.getUserHashtags();
    }
}
