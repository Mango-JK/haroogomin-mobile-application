package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Token;
import com.mango.harugomin.domain.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Transactional
    public Token save(Long userId, String jwt){
        Token token = new Token(userId, jwt);
        return tokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    public Optional<Token> findById(Long userId){
        return tokenRepository.findById(userId);
    }
}
