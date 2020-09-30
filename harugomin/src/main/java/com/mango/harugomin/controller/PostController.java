package com.mango.harugomin.controller;

import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.dto.PostResponseDto;
import com.mango.harugomin.dto.PostSaveRequestDto;
import com.mango.harugomin.dto.PostUpdateRequestDto;
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

import java.util.List;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "2. Post")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

    private final PostService postService;
    private final HashtagService hashtagService;

    /**
     * 1. 고민글 작성
     */
    @ApiOperation("고민글 작성")
    @PostMapping(value = "/posts")
    public ResponseEntity writePost(PostSaveRequestDto requestDto) throws Exception {
        PostResponseDto responseDto = null;
        try {
            responseDto = new PostResponseDto(postService.save(requestDto));
        } catch (Exception e) {
            return new ResponseEntity(responseDto, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    /**
     * 2. 고민글 수정
     */
    @ApiOperation("고민글 수정")
    @PutMapping(value = "/posts/{postId}")
    public ResponseEntity updatePost(@PathVariable("postId") Long postId, @RequestBody PostUpdateRequestDto requestDto) throws Exception {
        try {
            postService.updatePost(postId, requestDto);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 3. 고민글 삭제 (History로 이동)
     */
    @ApiOperation("고민글 삭제")
    @DeleteMapping(value = "/posts/{postId}")
    public ResponseEntity deletePost(@PathVariable("postId") Long postId) throws Exception {
        try {
            postService.deletePost(postId);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 4. 고민글 전체 조회
     */
    @ApiOperation("고민글 전체 조회")
    @GetMapping(value = "/posts")
    public ResponseEntity findAllPosts(@RequestParam("pageNum") int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
        Page<Post> result = null;
        try {
            result = postService.findAllPosts(pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 5. 고민글 상세 조회
     */
    @ApiOperation("고민글 상세 조회")
    @GetMapping(value = "/posts/{postId}")
    public ResponseEntity findOne(@PathVariable("postId") Long postId) {
        Post post = postService.findById(postId).get();
        if (post == null) {
            return new ResponseEntity(post, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(post, HttpStatus.OK);
    }

    /**
     * 6. 고민글 해시태그별 조회
     */
    @ApiOperation("고민글 해시태그별 조회")
    @GetMapping(value = "/posts/hashtag/{tagName}")
    public ResponseEntity findAllByHashtag(@PathVariable("tagName") String tagName, int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
        Page<Post> result = null;
        try {
            result = postService.findAllByHashtag(tagName, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 7. 고민글 검색
     */
    @ApiOperation("고민글 통합 검색")
    @GetMapping(value = "/posts/search/{keyword}")
    public ResponseEntity searchAllPosts(@PathVariable("keyword") String keyword) throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 15, Sort.by("created_date").descending());
        Page<Post> result = null;
        try {
            result = postService.searchAllPosts(keyword, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * 8. 두번째 탭
     */
//    @ApiOperation("hago Second Tab")
//    @GetMapping(value = "/posts/home")
//    public ResponseEntity homeView(@RequestParam("tagName") String tagName) {
//        PageRequest tagRequest = PageRequest.of(0, 12, Sort.by("posting_count"));
//        Object[] topTags = hashtagService.findAllTags(tagRequest).get().toArray();
//        for(Object tags : topTags) {
//            log.info(":::: " + tags.toString());
//        }
//
//
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("created_date").descending());
//        List<Post> story = postService.findAllPosts(pageRequest).getContent();
//
//        for(Post p : story) {
//            log.info(":::: " + p.getTitle() + ", " + p.getContent());
//        }
//
//
//
//        return new ResponseEntity(HttpStatus.NOT_FOUND);
//    }

}
