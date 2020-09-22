package com.mango.harugomin.controller;

import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.dto.PostRequestDto;
import com.mango.harugomin.dto.PostResponseDto;
import com.mango.harugomin.service.HashtagService;
import com.mango.harugomin.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "2. Post")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final PostService postService;
    private final HashtagService hashtagService;

    @ApiOperation("고민 작성")
    @PostMapping(value = "/posts")
    public ResponseEntity<PostResponseDto> writePost(PostRequestDto requestDto) {
        Long tagId = hashtagService.findByTagname(requestDto.getTagName()).getTagId();
        Post newPost = new Post(requestDto.getUserNickname(), requestDto.getTitle(), requestDto.getContent(), tagId);
        return new ResponseEntity(postService.save(newPost), HttpStatus.OK);
    }

    @ApiOperation("전체 고민글 조회")
    @GetMapping(value = "/posts")
    public ResponseEntity<List<PostResponseDto>> findAllPosts() {
        List<PostResponseDto> result = postService.findAllPosts();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
