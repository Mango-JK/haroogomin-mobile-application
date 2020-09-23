package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Post save(Post newPost) {
        return postRepository.save(newPost);
    }

    /**
     * 1. 모든 게시글 조회
     */
    public Page<Post> findAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * 태그별 페이징된 고민글 조회
     */
//    public Page<Post> findAllPosts(Long tagId, Pageable pageable) {
//        return postRepository.findAllByHashtag(tagId, pageable);
//    }
}
