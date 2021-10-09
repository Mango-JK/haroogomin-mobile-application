package com.mango.harugomin.controller;

import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.*;
import com.mango.harugomin.dto.PostSaveRequestDto;
import com.mango.harugomin.service.HashtagService;
import com.mango.harugomin.service.HistoryService;
import com.mango.harugomin.service.PostService;
import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "2. Post")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PostController {

	private final PostService postService;
	private final UserService userService;
	private final HashtagService hashtagService;
	private final HistoryService historyService;

	@ApiOperation("고민글 작성 or 수정")
	@PostMapping(value = "/posts")
	public ResponseEntity addPost(@RequestBody PostSaveRequestDto requestDto) {
		return postService.addPost(requestDto);
	}

	@ApiOperation("고민글 삭제")
	@DeleteMapping(value = "/posts/{postId}")
	public ResponseEntity deletePost(@PathVariable("postId") Long postId) {
		return postService.deletePost(postId);
	}

	@ApiOperation("고민글 상세 조회")
	@GetMapping(value = "/posts/{postId}")
	public ResponseEntity getPostDetails(@PathVariable("postId") Long postId) {
		return postService.getPostDetails(postId);
	}

	@ApiOperation("(Home) - 인기순 해시태그 리스트")
	@GetMapping(value = "/posts/home/hashtag")
	public ResponseEntity getHashtagByLankings() {
		return postService.getHashtagByPostingCount();
	}

	@ApiOperation("(Home) - 스토리")
	@GetMapping(value = "/posts/home/story")
	public ResponseEntity getStoryPosts() {
		return postService.getStoryPosts();
	}

	@ApiOperation("(HOME) - 태그별 새 고민글")
	@GetMapping(value = "/posts/home/{tagName}")
	public ResponseEntity homePosting(@PathVariable("tagName") String tagName, @RequestParam int pageNum) {
		return postService.getPostsByHashtag(tagName, pageNum);
	}

	@ApiOperation("고민글 통합 검색")
	@GetMapping(value = "/posts/search/{keyword}")
	public ResponseEntity searchAllPosts(@PathVariable("keyword") String keyword, @RequestParam int pageNum) {
		return postService.searchPostsByKeyword(keyword, pageNum);
	}

	@ApiOperation("메인 고민글 3개 출력")
	@GetMapping(value = "/posts/main")
	public ResponseEntity getMainPosts(Long userId) {
		return postService.getMainPosts(userId);
	}

	// TODO 업로드 & 다운로드 구현
	@ApiOperation("고민글 사진 업로드")
	@PostMapping(value = "/posts/image")
	public String uploadPostImage(@RequestParam MultipartFile files) throws IOException {
		try {
			JsonObject data = new JsonObject();
			String TARGET_DIR = "/home/ubuntu/hago/files/";
			String imagePath = FilenameUtils.getBaseName(files.getOriginalFilename());

			if (files.isEmpty()) {
				data.addProperty("imgPath", "");
				data.addProperty("status", String.valueOf(HttpStatus.OK));
				return data.toString();
			} else {
				String fileName = files.getOriginalFilename();
				String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase();
				File targetFile;

				SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMddHHmmss");
				imagePath += timeFormat.format(new Date()) + "." + fileNameExtension;
				targetFile = new File(TARGET_DIR + imagePath);
				log.info("Image uploaded : {}", targetFile);
				files.transferTo(targetFile);
			}

			data.addProperty("imgPath", imagePath);
			data.addProperty("status", String.valueOf(HttpStatus.OK));

			return data.toString();
		} catch (Exception e) {
			return HttpStatus.FORBIDDEN.toString();
		}
	}
}
