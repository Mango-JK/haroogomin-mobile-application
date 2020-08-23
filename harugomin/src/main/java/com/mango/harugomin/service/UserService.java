package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }
}
