package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    @Transactional
    public Hashtag saveHashtag(Hashtag hashtag){
        return hashtagRepository.save(hashtag);
    }

    @Transactional
	public long addUserHashtag(String tagName) {
		Hashtag hashtag = new Hashtag(tagName, 0);
		return saveHashtag(hashtag).getTagId();
	}
}
