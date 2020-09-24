package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Post> findAllPostsByHashtag(long tagId, PageRequest pageRequest) {
        return postRepository.findAllByTagId(tagId, pageRequest);
    }

    /**
     * 태그별 페이징된 고민글 조회
     */
//    public Page<Post> findAllPosts(Long tagId, Pageable pageable) {
//        return postRepository.findAllByHashtag(tagId, pageable);
//    }
}
