package com.mango.harugomin.controller;

import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserSignUpRequestDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api/v2")
@RestController
public class UserController {

	private final UserService userService;

	@ApiOperation("회원가입")
	@PostMapping("/users/signup")
	public String signup(UserSignUpRequestDto requestDto) {
		log.info(":: /users/signup API ::");
		return userService.signup(requestDto);
	}

	@ApiOperation("로그인")
	@PostMapping("/users/login")
	public String login(@RequestParam("id") String id, @RequestParam("password") String password) {
		log.info(":: /users/login ::");
		return userService.login(id, password);
	}

	@ApiOperation("유저 ID 중복검사")
	@GetMapping(value = "/users/check/id")
	public ResponseEntity<String> duplicationCheckById(@RequestParam("id") String id) {
		return userService.duplicationCheckById(id);
	}

	@ApiOperation("유저 닉네임 중복검사")
	@GetMapping(value = "/users/check/nickname")
	public ResponseEntity<String> duplicationCheckByNickname(@RequestParam("nickname") String nickname) {
		return userService.duplicationCheckByNickname(nickname);
	}

	@ApiOperation("토큰 검증")
	@GetMapping("/users/check")
	public ResponseEntity checkToken(@RequestHeader HttpHeaders headers) {
		return userService.checkToken(headers.get("jwt").get(0));
	}

	// TODO 로그인 시 S3 주소 설정 필요
//	@ApiOperation("네이버 로그인")
//	@PostMapping("/users/login/naver")
//	public String naverLogin(HttpServletRequest request) {
//		return userService.naverLogin(request);
//	}

	// TODO SNS 토큰 검증
//	@ApiOperation("SNS 토큰 검증")
//	@PostMapping("/users/check/sns")
//	public ResponseEntity checkSNSToken(HttpServletRequest request) {
//		User user = null;
//		if (jwtService.isUsable(request.getHeader("jwt"))) {
//			Object obj = jwtService.get("user");
//			user = userService.findById(Long.parseLong(obj.toString())).get();
//			UserTokenResponseDto result = new UserTokenResponseDto(user);
//			return new ResponseEntity<>(result, HttpStatus.OK);
//		}
//		return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
//	}

	@ApiOperation("유저 프로필 사진 업데이트")
	@PutMapping(value = "/users/profileImage/{id}")
	public String updateUserProfile(@PathVariable(value = "id") Long userId, @RequestParam MultipartFile files) {
		return userService.updateUserProfile(userId, files);
	}

	@ApiOperation("유저 프로필 업데이트 [사진, 닉네임, 연령대, 해시태그]")
	@PutMapping(value = "/users")
	public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody UserUpdateRequestDto requestDto) {
		return userService.updateUserInfo(requestDto);
	}

	@ApiOperation("유저 해시태그 업데이트")
	@PutMapping(value = "/users/hashtag/{id}")
	public ResponseEntity<UserResponseDto> updateUserHashtag(@PathVariable(value = "id") Long userId, @RequestParam String[] hashtags) {
		return userService.updateUserHashtagInfo(userId, hashtags);
	}

	@ApiOperation("유저 삭제")
	@DeleteMapping(value = "/users/{userId}")
	public ResponseEntity<Long> deleteUser(@PathVariable("userId") Long userId) {
		return userService.deleteUser(userId);
	}

	@ApiOperation("현재 게시중인 고민글")
	@GetMapping(value = "/users/posts/{userId}")
	public ResponseEntity userAnnouncingPosts(@PathVariable("userId") Long userId, @RequestParam int pageNum) {
		return userService.userAnnouncingPosts(userId, pageNum);
	}

	@ApiOperation("내 글 보관함")
	@GetMapping(value = "/users/history/{userId}")
	public ResponseEntity userHistoryPosts(@PathVariable("userId") Long userId, @RequestParam int pageNum) {
		return userService.userHistoryPosts(userId, pageNum);
	}
}
