//package com.mango.harugomin.service;
//
//import com.mango.harugomin.domain.entity.Hashtag;
//import com.mango.harugomin.domain.entity.User;
//import com.mango.harugomin.domain.entity.UserHashtag;
//import com.mango.harugomin.domain.repository.UserHashtagRepository;
//import com.mango.harugomin.dto.UserUpdateRequestDto;
//import junit.framework.TestCase;
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Transactional
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserServiceTest extends TestCase {
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    HashtagService hashtagService;
//
//    @Autowired
//    UserHashtagRepository userHashtagRepository;
//
//
//    @Test
//    public void 닉네임_중복검사(){
//        // given
//        List<UserHashtag> userHashtagList = new ArrayList<>();
//        userHashtagList.add(new UserHashtag());
//
//        User user = User.builder()
//                .userId(1L)
//                .nickname("user1")
//                .ageRange(20)
//                .enablePosting(1)
//                .point(0)
//                .profileImage("...")
//                .build();
//        userService.saveUser(user);
//
//        // when
//        Boolean test1 = userService.duplicationCheck("user");
//        Boolean test2 = userService.duplicationCheck("user1");
//
//        // then
//        Assertions.assertThat(test1).isEqualTo(true);
//        Assertions.assertThat(test2).isEqualTo(false);
//    }
//
//    @Test
//    public void 유저_해시태그_업데이트() {
//        // given
//        String[] hashtags = {"연애", "돈", "육아"};
//        User user = User.builder()
//                .userId(1L)
//                .nickname("user1")
//                .ageRange(20)
//                .enablePosting(1)
//                .point(0)
//                .profileImage("...")
//                .build();
//
//        // when
//        hashtagService.deleteUserHashtag(user.getUserId());
//        user.initHashtag();
//
//        for(String tagName : hashtags) {
//            user.addHashtag(userService.addUserHashtag(user.getUserId(), tagName));
//        }
//
//        // then
//        List<UserHashtag> result = user.getUserHashtags();
//        Assertions.assertThat(result.get(0).getHashtag().getTagName()).isEqualTo("연애");
//        Assertions.assertThat(result.get(1).getHashtag().getTagName()).isEqualTo("돈");
//        Assertions.assertThat(result.get(2).getHashtag().getTagName()).isEqualTo("육아");
//    }
//
//    @Test
//    public void 유저_해시태그_추가(){
//        // given
//        User user = User.builder()
//                .userId(1L)
//                .nickname("user1")
//                .ageRange(20)
//                .enablePosting(1)
//                .point(0)
//                .profileImage("...")
//                .build();
//        userService.saveUser(user);
//
//        Hashtag hashtag = Hashtag.builder()
//                .tagName("연애")
//                .postingCount(0)
//                .totalCount(0)
//                .build();
//
//        // when
//        UserHashtag userHashtag = new UserHashtag(user, hashtag);
//        userHashtagRepository.save(userHashtag);
//
//        // then
//        Assertions.assertThat(userHashtag.getUser()).isEqualTo(user);
//        Assertions.assertThat(userHashtag.getHashtag()).isEqualTo(hashtag);
//
//    }
//
//    @Test
//    public void 유저_프로필_업데이트() {
//        // given
//        User user = User.builder()
//                .userId(1L)
//                .nickname("user1")
//                .ageRange(20)
//                .enablePosting(1)
//                .point(0)
//                .profileImage("...")
//                .build();
//        userService.saveUser(user);
//
//        String[] userHashtags = {"기혼", "취업"};
//        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
//                .userId(1L)
//                .nickname("tester")
//                .ageRange(30)
//                .userHashtags(userHashtags)
//                .point(100)
//                .profileImage("img")
//                .enablePosting(1)
//                .build();
//
//        // when
//        user.updateProfile(requestDto);
//        User afterUser = userService.updateUserHashtag(user.getUserId(), requestDto.getUserHashtags());
//
//        // then
//        Assertions.assertThat(afterUser.getUserHashtags().get(0).getHashtag().getTagName()).isEqualTo("기혼");
//        Assertions.assertThat(user.getNickname()).isEqualTo("tester");
//        Assertions.assertThat(user.getProfileImage()).isEqualTo("img");
//        Assertions.assertThat(user.getAgeRange()).isEqualTo(30);
//    }
//
//    @Test
//    public void 포인트_업() {
//        // given
//        User user = userService.findById(1456638292);
//        int userPoint = user.getPoint();
//
//        // when
//        userService.upOnePoint(user.getUserId());
//
//        // then
//        User user2 = userService.findById(1456638292);
//        Assertions.assertThat(user2.getPoint()).isEqualTo(userPoint + 1);
//    }
//
//    @Test
//    public void 포인트_사용하기() {
//        // given
//        User user = userService.findById(1456638292);
//        int userPoint = user.getPoint();
//
//        // when
//        userService.useThreePoint(user.getUserId());
//
//        // then
//        User user2 = userService.findById(1456638292);
//        Assertions.assertThat(user2.getPoint()).isEqualTo(userPoint - 3);
//    }
//}