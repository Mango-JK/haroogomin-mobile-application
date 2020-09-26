package com.mango.harugomin.controller;

import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.dto.PostRequestDto;
import com.mango.harugomin.dto.PostResponseDto;
import com.mango.harugomin.service.HashtagService;
import com.mango.harugomin.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 조회수 순으로 페이징한 전체 고민글 조회
     */
    @ApiOperation("전체 고민글 조회")
    @GetMapping(value = "/posts")
    public ResponseEntity findAllPosts(@RequestParam("pageNum")  int pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("hits").descending());
        Page<Post> result = postService.findAllPosts(pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 조회수 높은 순으로 페이징된 고민글 조회
     */
    @ApiOperation("태그별 고민글 조회")
    @GetMapping(value = "/posts/{tagName}")
    public ResponseEntity findAllPostsByHashtag(@PathVariable("tagName") String tagName, @RequestParam("pageNum") int pageNum) {
        long tagId = hashtagService.findByTagname(tagName).getTagId();
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("hits").descending());
        Page<Post> result = postService.findAllPostsByHashtag(tagId, pageRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
