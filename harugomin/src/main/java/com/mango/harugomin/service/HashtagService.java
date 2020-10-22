package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Hashtag saveHashtag(Hashtag hashtag){
        return hashtagRepository.save(hashtag);
    }

    public Hashtag findByTagName(String tagName) {
        return hashtagRepository.findByTagName(tagName);
    }

    @Transactional
    public void countUp(long tagId) {
        hashtagRepository.countUp(tagId);
    }

    @Transactional(readOnly = true)
    public Page<Hashtag> findAllTags(Pageable pageable) {
        return hashtagRepository.findAll(pageable);
    }
}
