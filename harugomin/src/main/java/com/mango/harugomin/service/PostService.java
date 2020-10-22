package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.HistoryRepository;
import com.mango.harugomin.domain.repository.LikerRepository;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserService userService;
    private final HashtagService hashtagService;
    private final PostRepository postRepository;
    private final HistoryRepository historyRepository;
    private final LikerRepository likerRepository;

    @Transactional
    public Post save(PostSaveRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId()).get();
        Hashtag hashtag = hashtagService.findByTagName(requestDto.getTagName());
        hashtagService.countUp(hashtag.getTagId());

        return postRepository.save(Post.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .tagName(requestDto.getTagName())
                .postImage(requestDto.getPostImage())
                .hits(0)
                .build()
        );
    }

    @Transactional
    public Post updatePost(PostSaveRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).get();
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getTagName(), requestDto.getPostImage());
        return post;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post deleteTargetPost = postRepository.findById(postId).get();
        postRepository.delete(deleteTargetPost);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest);
    }

    @Transactional
    public Optional<Post> findById(Long postId) {
        postRepository.postHits(postId);
        return postRepository.findById(postId);
    }

    @Transactional(readOnly = true)
    public Page<Post> findAllByHashtag(String tagName, PageRequest pageRequest) {
        return postRepository.findAllByTagName(tagName, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<Post> searchAllPosts(String keyword, PageRequest pageRequest) {
        return postRepository.searchAllPosts(keyword, pageRequest);
    }

    @Transactional
    public void postHits(Long postId) {
        postRepository.postHits(postId);
    }

    @Transactional
    public void postToHistory(Long postId) {
        Post targetPost = postRepository.findById(postId).get();
        History history = new History(targetPost);
        historyRepository.save(history);
        likerRepository.deleteAllByPostId(postId);
        postRepository.deleteById(targetPost.getPostId());
    }

    public Page<Post> findAllByUserId(Long userId, PageRequest pageRequest) {
        return postRepository.findAllByUserUserId(userId, pageRequest);
    }

    @Transactional
    public void deleteUserPosts(Long userId) {
        postRepository.deleteAllByUserUserId(userId);
    }

    @Transactional
    public void foreignkeyOpen() {
        postRepository.foreignkeyOpen();
    }

    @Transactional
    public void foreignkeyClose() {
        postRepository.foreignkeyClose();
    }
}
