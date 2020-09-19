package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.entity.UserHashtag;
import com.mango.harugomin.domain.repository.UserHashtagRepository;
import com.mango.harugomin.domain.repository.UserRepository;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.dto.UserUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserHashtagRepository userHashtagRepository;

    private final HashtagService hashtagService;

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        User user = userRepository.findByUserId(id);
        return user;
    }

    /**
     * 1.닉네임 중복 검사
     */
    public boolean duplicationCheck(String nickname) {
        if(userRepository.countByNickname(nickname) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 2. 유저 해시태그 업데이트
     */
    @Transactional
    public User updateUserHashtag(Long userId, String[] hashtags) {
        User user = findById(userId);
        hashtagService.deleteUserHashtag(userId);
        user.initHashtag();
        for (String tagName : hashtags) {
            user.addHashtag(addUserHashtag(userId, tagName));
        }
        return user;
    }

    /**
     * 3. 해시태그 추가
     */
    @Transactional
    public UserHashtag addUserHashtag(Long userId, String tagName) {
        User user = findById(userId);
        Hashtag hashtag = hashtagService.findByTagname(tagName);

        UserHashtag userHashtag = new UserHashtag(user, hashtag);
        hashtagService.countUp(hashtag.getTagId());
        return userHashtagRepository.save(userHashtag);
    }

    /**
     * 4. 유저 프로필 업데이트 [사진, 닉네임, 연령대, 해시태그]
     */
    @Transactional
    public User updateUser(UserUpdateRequestDto requestDto) {
        User user = findById(requestDto.getUserId());
        user.updateProfile(requestDto);

        user = updateUserHashtag(requestDto.getUserId(), requestDto.getUserHashtags());

        userRepository.save(user);
        return userRepository.findByUserId(user.getUserId());
    }



    /**
     * 댓글 작성 시, User Point 1 증가
     */
    @Transactional
    public int upOnePoint(Long userId) {
        return userRepository.upOnePoint(userId);
    }

    /**
     * 포인트 사용 시, User Point 3 감소
     */
    @Transactional
    public int useThreePoint(Long userId) {
        return userRepository.useThreePoint(userId);
    }

}
