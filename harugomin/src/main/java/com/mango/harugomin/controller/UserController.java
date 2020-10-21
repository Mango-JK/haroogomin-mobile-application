package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.*;
import com.mango.harugomin.dto.UserRequestDto;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserTokenResponseDto;
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
import java.util.ArrayList;

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
    private final CommentService commentService;
    private final LikerService likerService;
    private final UserHashtagService userHashtagService;
    private final AppleAPIService appleAPIService;
    private static Long STATIC_USER_ID = 14L;

    @ApiOperation("카카오 로그인")
    @PostMapping("/users/login/kakao")
    @ResponseBody
    public String kakaoLogin(HttpServletRequest request) {
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

    @ApiOperation("네이버 로그인")
    @PostMapping("/users/login/naver")
    public String naverLogin(HttpServletRequest request) {
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

    @ApiOperation("애플 로그인")
    @PostMapping("/users/login/apple")
    public ResponseEntity appleLogin(ServicesResponse servicesResponse) {
        log.info("START APPLE LOGIN !");
        if (servicesResponse.getId_token() == null || servicesResponse.getCode() == null)
            return null;

        User user = null;
        boolean flag = true;
        while (flag) {
            log.info("### WHILE USER_ID : " + STATIC_USER_ID + "   ########");
            if (userService.findById(STATIC_USER_ID).isEmpty()) {
                flag = false;
                String image = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default01.jpg";
                User newUser = User.builder()
                        .userId(STATIC_USER_ID++)
                        .nickname("Apple_User")
                        .profileImage(image)
                        .ageRange(0)
                        .userHashtags(new ArrayList<>())
                        .build();
                user = userService.saveUser(newUser);
            } else {
                STATIC_USER_ID++;
            }
        }

        UserTokenResponseDto response = new UserTokenResponseDto(user);

        return new ResponseEntity<>(response, HttpStatus.OK);
//        String code = servicesResponse.getCode();
//        log.info(" CODE : " + code);
//        String client_secret = appleAPIService.getAppleClientSecret(servicesResponse.getId_token());
//        log.info("CLIENT SECRET : " + client_secret);
//
//        log.info("=========== GET PAYLOAD START ======================");
//        appleAPIService.getPayload(servicesResponse.getId_token());
//        log.info("============= GOOD ===================");
//
//        String userId = appleAPIService.getUserId(servicesResponse.getId_token());
//        log.info("USER ID : " + userId);
//
//        return appleAPIService.requestCodeValidations(client_secret, code, null);
    }

    @ApiOperation("토큰 검증")
    @PostMapping("/users/check")
    public ResponseEntity checkToken(HttpServletRequest request) {
        User user = null;
        if (jwtService.isUsable(request.getHeader("jwt"))) {
            Object obj = jwtService.get("user");
            user = userService.findById(Long.parseLong(obj.toString())).get();
        }

        UserTokenResponseDto result = new UserTokenResponseDto(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation("유저 프로필 사진 업데이트")
    @PutMapping(value = "/users/profileImage/{id}")
    public String updateUserProfile(@PathVariable(value = "id") Long userId, @RequestParam MultipartFile file) throws IOException {
        User user = userService.findById(userId).get();
        String imgPath = S3Service.CLOUD_FRONT_DOMAIN_NAME + s3Service.upload(user.getProfileImage(), file);
        user.updateUserImage(imgPath);
        userService.saveUser(user);
        JsonObject data = new JsonObject();
        data.addProperty("imgPath", imgPath);
        data.addProperty("status", String.valueOf(HttpStatus.OK));

        return data.toString();
    }

    @ApiOperation("유저 프로필 업데이트 [사진, 닉네임, 연령대, 해시태그]")
    @PutMapping(value = "/users")
    public ResponseEntity<UserResponseDto> updateUserProfile(@RequestBody UserUpdateRequestDto requestDto) {
        userService.updateUser(requestDto);
        User user = userService.findById(requestDto.getUserId()).get();
        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
    }

    @ApiOperation("유저 해시태그 업데이트")
    @PutMapping(value = "/users/hashtag/{id}")
    public ResponseEntity<UserResponseDto> updateUserHashtag(@PathVariable(value = "id") Long userId, @RequestParam String[] hashtags) {

        User user = userService.updateUserHashtag(userId, hashtags);

        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
    }

    @ApiOperation("유저 닉네임 중복검사")
    @GetMapping(value = "/users/check/{nickname}")
    public ResponseEntity<String> duplicationCheck(@PathVariable("nickname") String nickname) {
        JsonObject data = new JsonObject();
        data.addProperty("flag", userService.duplicationCheck(nickname));

        return new ResponseEntity<>(data.toString(), HttpStatus.OK);
    }

    @ApiOperation("유저 삭제")
    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<Long> deleteUser(@PathVariable("userId") Long userId) {
        try {
            postService.foreignkeyOpen();
            historyService.deleteUserHistories(userId);
            commentService.deleteByUserId(userId);
            likerService.deleteAllByUsers(userId);
            userHashtagService.deleteAllByUsers(userId);
            postService.deleteUserPosts(userId);
            userService.deleteById(userId);
            postService.foreignkeyClose();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("현재 게시중인 고민글")
    @GetMapping(value = "/users/posts/{userId}")
    public ResponseEntity myCurrentPosting(@PathVariable("userId") Long userId, @RequestParam int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
        Page<Post> result = null;

        try {
            result = postService.findAllByUserId(userId, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(result.getContent(), HttpStatus.OK);
    }

    @ApiOperation("내 글 보관함")
    @GetMapping(value = "/users/history/{userId}")
    public ResponseEntity myHistoryPost(@PathVariable("userId") Long userId, @RequestParam int pageNum) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum, 15, Sort.by("createdDate").descending());
        Page<History> result = null;
        try {
            result = historyService.myHistoryPost(userId, pageRequest);
        } catch (Exception e) {
            return new ResponseEntity(result.getContent(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getContent(), HttpStatus.OK);
    }

    @ApiOperation("(SERVER_TEST용)카카오 AccessToken 발급받기")
    @GetMapping(value = "/users/login/kakao")
    public String getKakaoCode(@RequestParam("code") String code) {
        ResponseEntity<String> AccessToken = kakaoAPIService.getAccessToken(code);
        log.info("AccessToken : " + AccessToken);
        return "index";
    }

    @ApiOperation("(SERVER_TEST용)네이버 AccessToken 발급받기")
    @GetMapping(value = "/users/login/naver")
    public String getNaverCode(@RequestParam(value = "code") String code,
                               @RequestParam(value = "state") String state) {
        ResponseEntity<String> AccessToken = naverAPIService.getAccessToken(code, state);
        return "index";
    }
}