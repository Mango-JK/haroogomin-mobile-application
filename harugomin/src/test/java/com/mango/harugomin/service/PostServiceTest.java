package com.mango.harugomin.service;

import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "hits");
        int postsNum = postService.findAllPosts(pageable).getSize();
        Assertions.assertThat(postsNum).isEqualTo(5);

        pageable = PageRequest.of(0, 15);
        postsNum = postService.findAllPosts(pageable).getSize();
        Assertions.assertThat(postsNum).isEqualTo(15);
    }

}