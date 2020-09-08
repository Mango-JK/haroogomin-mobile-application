package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Hashtag saveHashtag(Hashtag hashtag){
        return hashtagRepository.save(hashtag);
    }

    public Hashtag findByTagname(String hashtag) {
        return hashtagRepository.findByTagName(hashtag);
    }

    @Transactional
    public void countUp(long tagId) {
        hashtagRepository.countUp(tagId);
    }
}
