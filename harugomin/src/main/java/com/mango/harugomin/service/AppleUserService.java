package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.AppleUser;
import com.mango.harugomin.domain.repository.AppleUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppleUserService {
    private final AppleUserRepository appleUserRepository;

    @Transactional(readOnly = true)
    public Optional<AppleUser> findByCode(String userCode) {
        return appleUserRepository.findById(userCode);
    }

    @Transactional
    public void saveAppleUserKey(AppleUser appleUser) {
        appleUserRepository.save(appleUser);
    }
}
