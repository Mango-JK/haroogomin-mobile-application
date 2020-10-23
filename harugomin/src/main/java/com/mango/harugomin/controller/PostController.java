package com.mango.harugomin.controller;

import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.dto.PostResponseDto;
import com.mango.harugomin.dto.PostSaveRequestDto;
import com.mango.harugomin.service.HashtagService;
import com.mango.harugomin.service.PostService;
import com.mango.harugomin.service.S3Service;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final S3Service s3Service;

    @ApiOperation("고민글 작성 or 수정")
    @PostMapping(value = "/posts")
    public ResponseEntity updatePost(@RequestBody PostSaveRequestDto requestDto) throws Exception {
        Post post = null;
        try {
            if (requestDto.getPostId() == -1) {
                post = postService.save(requestDto);
            } else
                post = postService.updatePost(requestDto);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new PostResponseDto(post), HttpStatus.OK);
    }

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

    @ApiOperation("고민글 상세 조회")
    @GetMapping(value = "/posts/{postId}")
    public ResponseEntity findOne(@PathVariable("postId") Long postId) {
        Post post = postService.findById(postId).get();
        if (post == null) {
            return new ResponseEntity(post, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(new PostResponseDto(post), HttpStatus.OK);
    }

    @ApiOperation("(Home) - 인기순 해시태그 리스트")
    @GetMapping(value = "/posts/home/hashtag")
    public ResponseEntity homeBestHashtag() throws Exception {
        PageRequest tagRequest = PageRequest.of(0, 12, Sort.by("postingCount").descending());
        List<Hashtag> topTags = null;
        try {
            topTags = hashtagService.findAllTags(tagRequest).getContent();
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(topTags, HttpStatus.OK);
    }

    @ApiOperation("(Home) - 스토리")
    @GetMapping(value = "/posts/home/story")
    public ResponseEntity homeStory() throws Exception {
        List<Post> story = null;

        try {
            PageRequest storyRequest = PageRequest.of(0, 13, Sort.by("createdDate"));
            List<Post> data = postService.findAllPosts(storyRequest).getContent();
            story = new ArrayList<>();
            for (Post post : data) {
                story.add(post);
                if (story.size() > 9)
                    break;
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(story, HttpStatus.OK);
    }

    @ApiOperation("(HOME) - 태그별 새 고민글")
    @GetMapping(value = "/posts/home/{tagName}")
    public ResponseEntity homePosting(@PathVariable("tagName") String tagName, @RequestParam int pageNum) throws Exception {
        PageRequest pageRequest = null;
        Page<Post> result = null;

        if (tagName.equals("전체")) {
            pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").ascending());
            result = postService.findAllPosts(pageRequest);
            LocalDateTime currentTime = LocalDateTime.now();
            for (Post post : result) {
                Duration duration = Duration.between(post.getCreatedDate(), currentTime);
                long minute = duration.getSeconds();

                if (duration.getSeconds() >= 86400) {
                    postService.postToHistory(post.getPostId());
                } else
                    break;

                pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
                result = postService.findAllPosts(pageRequest);

            }
            return new ResponseEntity(result.getContent(), HttpStatus.OK);
        }

        try {
            pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
            result = postService.findAllByHashtag(tagName, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getContent(), HttpStatus.OK);
    }

    @ApiOperation("고민글 통합 검색")
    @GetMapping(value = "/posts/search/{keyword}")
    public ResponseEntity searchAllPosts(@PathVariable("keyword") String keyword, @RequestParam int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("created_date").descending());
        Page<Post> result = null;
        try {
            result = postService.searchAllPosts(keyword, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result.getContent(), HttpStatus.OK);
    }

    @ApiOperation("메인 고민글 3개 출력")
    @GetMapping(value = "/posts/main")
    public ResponseEntity mainView() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("hits").descending());
        Page<Post> result = null;
        try {
            result = postService.findAllPosts(pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result.getContent(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getContent(), HttpStatus.OK);
    }

    @ApiOperation("고민글 사진 업로드")
    @PostMapping(value = "/posts/image")
    public String uploadPostImage(@RequestParam MultipartFile file) throws IOException {
        try {
            String imgPath = S3Service.CLOUD_FRONT_DOMAIN_NAME + s3Service.upload(null, file);
            JsonObject data = new JsonObject();
            data.addProperty("imgPath", imgPath);
            data.addProperty("status", String.valueOf(HttpStatus.OK));
            return data.toString();
        } catch (Exception e) {
            return HttpStatus.FORBIDDEN.toString();
        }
    }
}
