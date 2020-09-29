package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.PostSaveRequestDto;
import com.mango.harugomin.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserService userService;
    private final HashtagService hashtagService;
    private final PostRepository postRepository;

    /**
     * 1. 고민글 작성
     */
    @Transactional
    public Post save(PostSaveRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId());
        Hashtag hashtag = hashtagService.findByTagName(requestDto.getTagName());
        hashtagService.countUp(hashtag.getTagId());

        return postRepository.save(Post.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .tagName(requestDto.getTagName())
                .postImage(requestDto.getPostImage())
                .hits(0)
                .postLikes(0)
                .build()
        );
    }

    /**
     * 2. 고민글 수정
     */
    @Transactional
    public void updatePost(Long postId, PostUpdateRequestDto requestDto) {
        Post post = postRepository.findById(postId).get();
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getTagName(), requestDto.getPostImage());
    }

    /**
     * 3. 고민글 삭제
     */
    @Transactional
    public void deletePost(Long postId) {
        Post deleteTargetPost = postRepository.findById(postId).get();
        postRepository.delete(deleteTargetPost);
    }


    /**
     * 4. 모든 고민글 조회
     */
    @Transactional(readOnly = true)
    public Page<Post> findAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }


    /**
     * 5. 고민글 상세 조회
     */
    @Transactional(readOnly = true)
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    /**
     * 6. 해시태그별 고민글 전체 조회
     */
    @Transactional(readOnly = true)
    public Page<Post> findAllByHashtag(String tagName, PageRequest pageRequest) {
        return postRepository.findAllByTagName(tagName, pageRequest);
    }

    /**
     * 제목, 내용에서 keyword로 검색
     */
    @Transactional(readOnly = true)
    public Page<Post> searchAllPosts(String keyword, PageRequest pageRequest) {
        return postRepository.searchAllPosts(keyword, pageRequest);
    }
}
