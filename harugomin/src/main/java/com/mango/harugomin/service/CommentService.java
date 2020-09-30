package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Comment;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.CommentRepository;
import com.mango.harugomin.dto.CommentSaveRequestDto;
import com.mango.harugomin.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

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
        post.addComment(comment);
        commentRepository.save(comment);

        return comment;
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.update(requestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        Post post = postService.findById(comment.getPost().getPostId()).get();
        post.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
