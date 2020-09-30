package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.UserHashtag;
import com.mango.harugomin.domain.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserHashtagService {

    private final UserHashtagRepository userHashtagRepository;

    public void deleteUserHashtag(Long userId){
        userHashtagRepository.deleteByUserId(userId);
    }

    public void addUserHashtag(UserHashtag newUserHashtag) {
        userHashtagRepository.save(newUserHashtag);
    }
}
