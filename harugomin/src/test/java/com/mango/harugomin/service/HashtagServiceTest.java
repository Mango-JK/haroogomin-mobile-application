package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class HashtagServiceTest extends TestCase {

    @Autowired HashtagService hashtagService;

    @Test
    public void 해시태그_카운트올리기(){
//        // given
        Hashtag hashtag = hashtagService.findByTagname("테스트");
        long postingCount = hashtag.getPostingCount();
        long totalCount = hashtag.getTotalCount();

        // when
        hashtagService.countUp(hashtag.getTagId());

        // then
        Hashtag hashtag1 = hashtagService.findByTagname("테스트");
        Assertions.assertThat(postingCount).isEqualTo(hashtag1.getPostingCount() - 1);
        Assertions.assertThat(totalCount).isEqualTo(hashtag1.getTotalCount() - 1);

    }
}