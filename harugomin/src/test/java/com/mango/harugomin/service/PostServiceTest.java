package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Post;
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
public class PostServiceTest extends TestCase {

    @Autowired
    PostService postService;

    @Test
    public void testFindAllPosts() {
        // given
        Post newPost = new Post("정건", "제목1", "내용1", 1L);
        Post newPost3 = new Post("정건", "제목3", "내용3", 3L);

        // when
        int currentPostNum = postService.findAllPosts().size();
        postService.save(newPost);
        Assertions.assertThat(postService.findAllPosts().size()).isEqualTo(currentPostNum + 1);

        // then
        postService.save(newPost3);
        Assertions.assertThat(postService.findAllPosts().size()).isEqualTo(currentPostNum + 2);

    }
}