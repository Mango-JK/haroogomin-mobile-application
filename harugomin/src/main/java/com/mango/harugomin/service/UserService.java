package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
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
    private final HashtagService hashtagService;
    private final S3Service s3Service;

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




    @Transactional
    public int upOnePoint(Long userId) {
        return userRepository.upOnePoint(userId);
    }

    @Transactional
    public int useThreePoint(Long userId) {
        return userRepository.useThreePoint(userId);
    }
}
