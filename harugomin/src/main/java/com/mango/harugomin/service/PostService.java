package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.domain.repository.HistoryRepository;
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

    /**
     * 1. 고민글 작성
     */
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
                .postLikes(0)
                .build()
        );
    }

    /**
     * 2. 고민글 수정
     */
    @Transactional
    public void updatePost(PostSaveRequestDto requestDto) {
        Post post = postRepository.findById(requestDto.getPostId()).get();
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
    public Page<Post> findAllPosts(PageRequest pageRequest) {
        return postRepository.findAll(pageRequest);
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
     * 7. 제목, 내용에서 keyword로 검색
     */
    @Transactional(readOnly = true)
    public Page<Post> searchAllPosts(String keyword, PageRequest pageRequest) {
        return postRepository.searchAllPosts(keyword, pageRequest);
    }

    /**
     * 8. 고민글 조회수 카운팅
     */
    @Transactional
    public void postHits(Long postId) {
        postRepository.postHits(postId);
    }

    /**
     * 9. 하루 지난 고민글 History로 이동
     */
    @Transactional
    public void postToHistory(Long postId) {
        Post targetPost = postRepository.findById(postId).get();
        History history = new History(targetPost);
        historyRepository.save(history);
        postRepository.delete(targetPost);
    }

    /**
     * 10. 현재 게시중인 글
     */
    public Page<Post> findAllByUserId(Long userId, PageRequest pageRequest) {
        return postRepository.findAllByUserUserId(userId, pageRequest);
    }

    /**
     * 11. 유저가 작성한 게시글 삭제
     */
    @Transactional
    public void deleteUserPosts(Long userId) {
        postRepository.deleteByUserUserId(userId);
    }
}
