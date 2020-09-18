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

import java.util.ArrayList;
import java.util.List;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest extends TestCase {

    @Autowired
    UserService userService;

    @Test
    public void 닉네임_중복검사(){
        // given
        List<UserHashtag> userHashtagList = new ArrayList<>();
        userHashtagList.add(new UserHashtag());

        User user = User.builder()
                .userId(1L)
                .nickname("user1")
                .userHashtag(userHashtagList)
                .ageRange("20")
                .enablePosting(1)
                .point(0)
                .profileImage("...")
                .build();
        userService.saveUser(user);

        // when
        Boolean test1 = userService.duplicationCheck("user");
        Boolean test2 = userService.duplicationCheck("user1");

        // then
        Assertions.assertThat(test1).isEqualTo(true);
        Assertions.assertThat(test2).isEqualTo(false);
    }

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
}