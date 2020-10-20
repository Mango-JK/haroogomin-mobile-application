package com.mango.harugomin.dto;

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
public class UserTokenResponseDto {
    private Long userId;
    private String nickname;
    private String profileImage;
    private int ageRange;
    private List<String> userHashtags = new ArrayList<>();

    public UserTokenResponseDto(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.profileImage = entity.getProfileImage();
        this.ageRange = entity.getAgeRange();
        for (UserHashtag hashtag : entity.getUserHashtags()) {
            this.userHashtags.add(hashtag.getHashtag().getTagName());
        }
    }
}
