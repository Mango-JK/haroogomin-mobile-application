package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.entity.UserHashtag;
import com.mango.harugomin.domain.repository.UserHashtagRepository;
import com.mango.harugomin.domain.repository.UserRepository;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final HashtagService hashtagService;
    private final UserHashtagService userHashtagService;

    private final UserRepository userRepository;
    private final UserHashtagRepository userHashtagRepository;

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    public boolean duplicationCheck(String nickname) {
        if(userRepository.countByNickname(nickname) > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public User updateUserHashtag(Long userId, String[] hashtags) {
        User user = findById(userId).get();
        userHashtagRepository.deleteByUserId(userId);
        user.initHashtag();

        for (String tagName : hashtags) {
            Hashtag hashtag = hashtagService.findByTagName(tagName);
            UserHashtag newUserHashtag = new UserHashtag(user, hashtag);
            hashtagService.countUp(hashtag.getTagId());

            userHashtagService.addUserHashtag(newUserHashtag);
            user.userAddTag(newUserHashtag);
        }
        userRepository.save(user);

        return user;
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto requestDto) {
        User user = findById(requestDto.getUserId()).get();
        user.updateUserProfile(requestDto);

        user = updateUserHashtag(requestDto.getUserId(), requestDto.getUserHashtags());

        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long userId) {
        userRepository.deleteUser(userId);
    }
}
