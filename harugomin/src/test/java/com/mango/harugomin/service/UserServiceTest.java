package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
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
public class UserServiceTest extends TestCase {

    @Autowired
    UserService userService;

    @Test
    public void 포인트_업() {
        // given
        User user = userService.findById(1456638292);
        int userPoint = user.getPoint();

        // when
        userService.upOnePoint(user.getUserId());

        // then
        User user2 = userService.findById(1456638292);
        Assertions.assertThat(user2.getPoint()).isEqualTo(userPoint + 1);
    }

    @Test
    public void 포인트_사용하기() {
        // given
        User user = userService.findById(1456638292);
        int userPoint = user.getPoint();

        // when
        userService.useThreePoint(user.getUserId());

        // then
        User user2 = userService.findById(1456638292);
        Assertions.assertThat(user2.getPoint()).isEqualTo(userPoint - 3);
    }

    @Test
    public void 유저_해시태그_수정() {
        // given
        User user = userService.findById(1456638292);
        Hashtag hashtag = user.getUserHashtag();
        String tagName = hashtag.getTagName();

        // when
        userService.updateUserHashtag(1456638292L, "테스트");

        // then
        Assertions.assertThat(userService.findById(1456638292).getUserHashtag().getTagName()).isEqualTo("테스트");
    }
}