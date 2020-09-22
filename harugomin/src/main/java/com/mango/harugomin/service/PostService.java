package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
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
    public List<PostResponseDto> findAllPosts() {
        List<Post> findAllPost = postRepository.findAll();
        System.out.println("findAllPost SIze : " + findAllPost.size());
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : findAllPost) {
            responseDtos.add(new PostResponseDto(post));
        }
        return responseDtos;
    }

}
