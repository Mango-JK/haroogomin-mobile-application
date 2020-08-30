package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(long id) {
        User user = userRepository.findByUserId(id);
        return user;
    }
}
