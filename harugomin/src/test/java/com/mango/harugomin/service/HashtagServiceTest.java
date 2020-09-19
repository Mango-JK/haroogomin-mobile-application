package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.entity.UserHashtag;
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
    @Autowired UserService userService;

    @Test
    public void 해시태그_카운트올리기(){
//        // given
        Hashtag hashtag = hashtagService.findByTagname("돈");
        long postingCount = hashtag.getPostingCount();
        long totalCount = hashtag.getTotalCount();

        // when
        hashtagService.countUp(hashtag.getTagId());

        // then
        Hashtag hashtag1 = hashtagService.findByTagname("돈");
        Assertions.assertThat(postingCount).isEqualTo(hashtag1.getPostingCount() - 1);
        Assertions.assertThat(totalCount).isEqualTo(hashtag1.getTotalCount() - 1);
    }

    @Test
    public void 유저_해시태그_삭제() {
        // given
        User user = User.builder()
                .userId(1L)
                .nickname("user1")
                .ageRange(20)
                .enablePosting(1)
                .point(0)
                .profileImage("...")
                .build();

        Hashtag hashtag = Hashtag.builder()
                .tagName("돈")
                .postingCount(0)
                .totalCount(0)
                .build();

        user.getUserHashtags().add(new UserHashtag(user, hashtag));
        userService.saveUser(user);

        // when
        System.out.println("저장한 태그 이름 : " + user.getUserHashtags().get(0).getHashtag().getTagName());
        Assertions.assertThat(userService.findById(1L).getUserHashtags().size()).isEqualTo(1);
        hashtagService.deleteUserHashtag(1L);

        // then
        Assertions.assertThat(userService.findById(1L).getUserHashtags().size()).isEqualTo(0);

    }
}