package com.mango.harugomin.service;

import com.mango.harugomin.domain.entity.Comment;
import com.mango.harugomin.domain.entity.Liker;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.repository.CommentRepository;
import com.mango.harugomin.domain.repository.LikerRepository;
import com.mango.harugomin.domain.repository.PostRepository;
import com.mango.harugomin.dto.CommentResponseDto;
import com.mango.harugomin.dto.CommentSaveRequestDto;
import com.mango.harugomin.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final LikerRepository likerRepository;

	public Optional<Comment> findById(Long commentId) {
		return commentRepository.findById(commentId);
	}

	@Transactional
	public ResponseEntity writeComment(CommentSaveRequestDto requestDto) {
		CommentResponseDto responseDto = null;
		try {
			responseDto = new CommentResponseDto(save(requestDto));
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(responseDto, HttpStatus.OK);
	}

	@Transactional
	public Comment save(CommentSaveRequestDto requestDto) {
		Post post = postRepository.findById(requestDto.getPostId()).get();
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
	public ResponseEntity updateComment(Long commentId, CommentUpdateRequestDto requestDto) {
		try {
			Comment comment = commentRepository.findById(commentId).get();
			Post post = postRepository.findById(requestDto.getPostId()).get();
			post.getComments().remove(comment);
			comment.update(requestDto.getContent());
			post.getComments().add(comment);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity deleteComment(Long commentId) {
		try {
			Comment comment = commentRepository.findById(commentId).get();
			Post post = postRepository.findById(comment.getPost().getPostId()).get();
			post.getComments().remove(comment);
			post.downCommentCount();
			commentRepository.delete(comment);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@Transactional(readOnly = true)
	public Page<Comment> findAllByPostPostId(Long postId, Pageable pageable) {
		return commentRepository.findAllByPostPostId(postId, pageable);
	}

	@Transactional
	public void likeUpdate(Long commentId, int value) {
		commentRepository.likeUpdate(commentId, value);
	}

	@Transactional
	public ResponseEntity likeComment(Long commentId, Long userId) {
		Comment comment = findById(commentId).get();
		try {
			if (likerRepository.findLiker(commentId, userId) > 0) {
				Liker liker = likerRepository.findByComment_CommentIdAndUserId(commentId, userId).get();
				likerRepository.delete(liker);
				likeUpdate(commentId, -1);
			} else {
				Liker liker = new Liker(userId, comment);
				likerRepository.save(liker);
				likeUpdate(commentId, 1);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	public ResponseEntity findCommentsByPost(Long postId, long userId, int pageNum) {
		PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").ascending());
		List<Comment> result = findAllByPostPostId(postId, pageRequest).getContent();
		if (result == null) {
			new ResponseEntity(HttpStatus.OK);
		}

		List<Long> likers = likerRepository.findAllByUserId(userId);
		if(likers.isEmpty())
			return new ResponseEntity(result, HttpStatus.OK);
		else {
			for(Comment comment : result) {
				if(likers.contains(comment.getCommentId())){
					comment.userLikeThis();
				}
			}
		}
		return new ResponseEntity(result, HttpStatus.OK);
	}
}
