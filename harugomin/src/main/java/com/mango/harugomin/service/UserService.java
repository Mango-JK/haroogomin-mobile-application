package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
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

    @Transactional
    public int updateUserHashtag(Long userId, String hashtag) {
        long tagId = hashtagService.findByHashTag(hashtag);
        return userRepository.updateUserHashTag(userId, tagId);
    }
}
