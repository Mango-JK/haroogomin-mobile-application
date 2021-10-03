package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.UserHashtag;
import com.mango.harugomin.domain.repository.UserHashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserHashtagService {

    private final UserHashtagRepository userHashtagRepository;

    @Transactional
    public void deleteAllByUsers(Long userId) {
        userHashtagRepository.deleteByUserId(userId);
    }
}
