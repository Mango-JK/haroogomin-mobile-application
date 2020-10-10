package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Comment;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.CommentRepository;
import com.mango.harugomin.dto.CommentSaveRequestDto;
import com.mango.harugomin.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public Optional<Comment> findById(Long commentId){
        return commentRepository.findById(commentId);
    }

    @Transactional
    public Comment save(CommentSaveRequestDto requestDto) {
        Post post = postService.findById(requestDto.getPostId()).get();
        Comment comment = commentRepository.save(Comment.builder()
                .userId(requestDto.getUserId())
                .nickname(requestDto.getNickname())
                .profileImage(requestDto.getProfileImage())
                .post(post)
                .content(requestDto.getContent())
                .commentLikes(0)
                .build()
        );
        post.upCommentCount();
        post.addComment(comment);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).get();
        Post post = postService.findById(requestDto.getPostId()).get();
        post.getComments().remove(comment);
        comment.update(requestDto.getContent());
        post.getComments().add(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        Post post = postService.findById(comment.getPost().getPostId()).get();
        post.getComments().remove(comment);
        post.downCommentCount();
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findAllByPostPostId(Long postId, Pageable pageable){
        return commentRepository.findAllByPostPostId(postId, pageable);
    }

    @Transactional
    public void likeUpdate(Long commentId, int value) {
        commentRepository.likeUpdate(commentId, value);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        commentRepository.deleteAllByUserId(userId);
    }
}
