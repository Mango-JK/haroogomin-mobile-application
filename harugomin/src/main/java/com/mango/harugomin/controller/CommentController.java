package com.mango.harugomin.controller;

import com.mango.harugomin.dto.CommentSaveRequestDto;
import com.mango.harugomin.dto.CommentUpdateRequestDto;
import com.mango.harugomin.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "3. Comment")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class CommentController {

    private final CommentService commentService;

    @ApiOperation("댓글 작성")
    @PostMapping(value = "/comments")
    public ResponseEntity writeComment(@RequestBody CommentSaveRequestDto requestDto) {
    	return commentService.writeComment(requestDto);
    }

    @ApiOperation("댓글 수정")
    @PutMapping(value = "/comments/{commentId}")
    public ResponseEntity updateComment(@PathVariable("commentId") Long commentId, @RequestBody CommentUpdateRequestDto requestDto) {
    	return commentService.updateComment(commentId, requestDto);
    }

    @ApiOperation("댓글 삭제")
    @DeleteMapping(value = "/comments/{commentId}")
    public ResponseEntity deletePost(@PathVariable("commentId") Long commentId) {
    	return commentService.deleteComment(commentId);
    }

    @ApiOperation("댓글 좋아요")
    @PutMapping(value = "/comments/like")
    public ResponseEntity likeComment(@RequestParam("commentId") Long commentId, @RequestParam("userId") Long userId) {
    	return commentService.likeComment(commentId, userId);
    }

    @ApiOperation("댓글 조회 (페이징)")
    @GetMapping(value = "/comments/{postId}")
    public ResponseEntity findComments(@PathVariable("postId") Long postId, @RequestParam("userId") long userId, @RequestParam("pageNum") int pageNum) {
        return commentService.findCommentsByPost(postId, userId, pageNum);
    }
}
