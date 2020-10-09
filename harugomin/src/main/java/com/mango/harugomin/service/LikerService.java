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
    public int findLiker(Long commentId, Long userId){
        return likerRepository.findLiker(commentId, userId);
    }

    @Transactional
    public void deteleLike(Long commentId, Long userId) {
        Liker liker = likerRepository.findByComment_CommentIdAndUserId(commentId, userId).get();
        likerRepository.delete(liker);
    }

    @Transactional
    public void deleteAllByUsers(Long userId){
        likerRepository.deleteAllbyUsers(userId);
    }

    @Transactional
    public void save(Liker liker) {
        likerRepository.save(liker);
    }
}
