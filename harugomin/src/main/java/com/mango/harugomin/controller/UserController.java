package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.History;
import com.mango.harugomin.domain.entity.Post;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.jwt.JwtService;
import com.mango.harugomin.service.*;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;
    private final KakaoAPIService kakaoAPIService;
    private final NaverAPIService naverAPIService;
    private final JwtService jwtService;
    private final S3Service s3Service;
    private final HistoryService historyService;
    private final PostService postService;

    /**
     * 1. 카카오 로그인
     */
    @ApiOperation("카카오 로그인")
    @PostMapping("/users/login/kakao")
    @ResponseBody
    public String kakaoLogin(HttpServletRequest request) {
        log.info("POST :: /user/login/kakao");

        String accessToken = request.getHeader("accessToken");
        JsonNode json = kakaoAPIService.getKaKaoUserInfo(accessToken);

        String result = null;
        JsonObject data = new JsonObject();
        try {
            result = kakaoAPIService.redirectToken(json); // 토큰 발행
        } catch (Exception e) {
            log.error(e + "");
            data.addProperty("jwt", result);
            data.addProperty("status", String.valueOf(HttpStatus.BAD_REQUEST));
            return data.toString();
        }
        data.addProperty("jwt", result);
        data.addProperty("status", String.valueOf(HttpStatus.OK));
        return data.toString();
    }

    /**
     * 2. 네이버 로그인
     */
    @ApiOperation("네이버 로그인")
    @PostMapping("/users/login/naver")
    public String naverLogin(HttpServletRequest request) {
        log.info("POST :: /user/login/naver");

        String accessToken = request.getHeader("accessToken");
        JsonNode json = naverAPIService.getNaverUserInfo(accessToken);

        String result = null;
        JsonObject data = new JsonObject();
        try {
            result = naverAPIService.redirectToken(json);
        } catch (Exception e) {
            data.addProperty("jwt", result);
            data.addProperty("status", String.valueOf(HttpStatus.BAD_REQUEST));
            return data.toString();
        }
        data.addProperty("jwt", result);
        data.addProperty("status", String.valueOf(HttpStatus.OK));
        return data.toString();
    }

    /**
     * 3. 토큰 검증
     */
    @ApiOperation("토큰 검증")
    @PostMapping("/users/check")
    public Object checkToken(@RequestParam String jwtToken) {
        log.info("UserController : checkToken");

        Object result = null;

        if (jwtService.isUsable(jwtToken)) {
            result = jwtService.get("user");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 4. 프로필 사진 등록
     */
    @ApiOperation("유저 프로필 사진 업데이트")
    @PutMapping(value = "/users/profileImage/{id}")
    public String updateUserProfile(@PathVariable(value = "id") Long userId, @RequestParam MultipartFile file) throws IOException {
        User user = userService.findById(userId);
        String imgPath = S3Service.CLOUD_FRONT_DOMAIN_NAME + s3Service.upload(user.getProfileImage(), file);
        user.updateUserImage(imgPath);
        userService.saveUser(user);
        JsonObject data = new JsonObject();
        data.addProperty("imgPath", imgPath);
        data.addProperty("status", String.valueOf(HttpStatus.OK));

        return data.toString();
    }

    /**
     * 5. 프로필 업데이트
     */
    @ApiOperation("유저 프로필 업데이트 [사진, 닉네임, 연령대, 해시태그]")
    @PutMapping(value = "/users")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody UserUpdateRequestDto requestDto) {
        userService.updateUser(requestDto);
        User user = userService.findById(requestDto.getUserId());
        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
    }

    /**
     * 6. 유저 해시태그 업데이트
     */
    @ApiOperation("유저 해시태그 업데이트")
    @PutMapping(value = "/users/hashtag/{id}")
    public ResponseEntity<UserResponseDto> updateUserHashtag(@PathVariable(value = "id") Long userId, @RequestParam String[] hashtags) {

        User user = userService.updateUserHashtag(userId, hashtags);

        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
    }

    /**
     * 7. 닉네임 중복검사
     */
    @ApiOperation("유저 닉네임 중복검사")
    @GetMapping(value = "/users/check/{nickname}")
    public ResponseEntity<String> duplicationCheck(@PathVariable("nickname") String nickname) {
        JsonObject data = new JsonObject();
        data.addProperty("flag", userService.duplicationCheck(nickname));

        return new ResponseEntity<>(data.toString(), HttpStatus.OK);
    }

//    /**
//     * 8. 유저 삭제
//     */
//    @ApiOperation("유저 삭제")
//    @GetMapping(value = "/users/{userId}")
//    public ResponseEntity<Long> deleteUser(@PathVariable("userId") Long userId) {
//        // 글 삭제
//
//        // 댓글 삭제
//
//        return new ResponseEntity<>(deleteUserId, HttpStatus.OK);
//    }

    /**
     * 9. 현재 게시중인 글
     */
    @ApiOperation("현재 게시중인 고민글")
    @GetMapping(value = "/users/posts/{userId}")
    public Optional<List<Post>> myCurrentPosting(@PathVariable("userId") Long userId) throws Exception{
        return postService.findAllByUserId(userId);
    }

    /**
     * 10. 내 글 보관함
     */
    @ApiOperation("내 글 보관함")
    @GetMapping(value = "/users/history/{userId}")
    public ResponseEntity myHistoryPost(@PathVariable("userId") Long userId, @RequestParam int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
        Page<History> result = null;
        try {
            result = historyService.myHistoryPost(userId, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @ApiOperation("(SERVER_TEST용)카카오 AccessToken 발급받기")
    @GetMapping(value = "/users/login/kakao")
    public String getKakaoCode(@RequestParam("code") String code) {
        log.info("User Kakao Code : " + code);

        ResponseEntity<String> AccessToken = kakaoAPIService.getAccessToken(code);

        log.info("My AccessToken : " + AccessToken);
        return "index";
    }


    @ApiOperation("(SERVER_TEST용)네이버 AccessToken 발급받기")
    @GetMapping(value = "/users/login/naver")
    public String getNaverCode(@RequestParam(value = "code") String code,
                               @RequestParam(value = "state") String state) {
        log.info("User Naver Code : " + code);
        log.info("State Code : " + state);

        ResponseEntity<String> AccessToken = naverAPIService.getAccessToken(code, state);

        log.info("Naver AccessToken : " + AccessToken);
        return "index";
    }

}