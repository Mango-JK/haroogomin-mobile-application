package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Liker;
import com.mango.harugomin.domain.repository.LikerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikerService {
    private final LikerRepository likerRepository;

    @Transactional
    public void save(Liker liker) {
        likerRepository.save(liker);
    }
}
