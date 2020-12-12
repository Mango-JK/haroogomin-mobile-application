package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import com.mango.harugomin.domain.entity.*;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserSignUpRequestDto;
import com.mango.harugomin.dto.UserTokenResponseDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.jwt.JwtService;
import com.mango.harugomin.service.*;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*")
@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final HistoryService historyService;
    private final PostService postService;
    private final CommentService commentService;
    private final LikerService likerService;
    private final UserHashtagService userHashtagService;
    private final TokenService tokenService;
    private final NaverApiService naverAPIService;

    @ApiOperation("회원가입")
    @PostMapping("/users/singup")
    @ResponseBody
    public ResponseEntity signUp(UserSignUpRequestDto requestDto) {
        log.info(":: /users/signup API ::");
        User newUser = User.builder()
                .userLoginId(requestDto.getUserLoginId())
                .password(requestDto.getPassword())
                .nickname(requestDto.getNickname())
                .profileImage(requestDto.getProfileImage())
                .ageRange(requestDto.getAgeRange())
                .build();

        Long userId = userService.save(newUser).getUserId();
        userService.updateUserHashtag(userId, requestDto.getUserhashtags());

        try {
            User user = userService.findById(userId).get();
            String jwt = jwtService.create("user", user, "user");
            tokenService.save(userId, jwt);
            return new ResponseEntity(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("JWT Create Token Error : " + e);
            return new ResponseEntity("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("로그인")
    @PostMapping("/users/login")
    @ResponseBody
    public ResponseEntity login(@RequestParam("id") String id, @RequestParam("password") String password){
        log.info(":: /users/login ::");
        Optional<User> user = userService.findByUserLoginId(id);
        if(!user.isEmpty()){
            User loginUser = user.get();
            if(!password.equals(loginUser.getPassword())){
                return new ResponseEntity("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity("ID가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Long userId = user.get().getUserId();
        String jwt = tokenService.findById(userId).get().getJwt();

        return new ResponseEntity(jwt, HttpStatus.OK);
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
            data.addProperty("status", String.valueOf(HttpStatus.BAD_REQUEST));
            return data.toString();
        }
        data.addProperty("jwt", result);
        data.addProperty("status", String.valueOf(HttpStatus.OK));
        return data.toString();
    }

    @ApiOperation("토큰 검증")
    @PostMapping("/users/check")
    public ResponseEntity checkToken(HttpServletRequest request) {
        log.info(":: /users/check ::");
        User user = null;
        if (jwtService.isUsable(request.getHeader("jwt"))) {
            Object obj = jwtService.get("user");
            log.info("obj : " + obj.toString());
            user = userService.findById(Long.parseLong(obj.toString())).get();
        } else {
            return new ResponseEntity("유효하지 않은 토큰입니다.", HttpStatus.NOT_FOUND);
        }

        log.info("before result");
        UserTokenResponseDto result = new UserTokenResponseDto(user);
        log.info("result : " + result);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation("SNS 토큰 검증")
    @PostMapping("/users/check/sns")
    public ResponseEntity checkSNSToken(HttpServletRequest request) {
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
    public String updateUserProfile(@PathVariable(value = "id") Long userId, @RequestParam MultipartFile files) throws IOException {
        String targetFilePath = "/home/ubuntu/images/profile/";
        JsonObject data = new JsonObject();
        User user = userService.findById(userId).get();
        String imagePath = "";

        if(files.isEmpty()) {
            data.addProperty("imgPath", "");
            data.addProperty("status", String.valueOf(HttpStatus.OK));
            return data.toString();
        } else {
            String fileName = files.getOriginalFilename();
            String fileNameExtension = FilenameUtils.getExtension(fileName).toLowerCase();
            File targetFile;
            String TF_Name;

            SimpleDateFormat timeFormat = new SimpleDateFormat("yyMMddHHmmss");
            TF_Name = timeFormat.format(new Date()) + "." + fileNameExtension;
            imagePath = targetFilePath+TF_Name;
            targetFile = new File(imagePath);
            log.info("Image uploaded : {}", targetFile);
            files.transferTo(targetFile);
        }

        user.updateUserImage(imagePath);
        userService.save(user);

        data.addProperty("imgPath", imagePath);
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

    @ApiOperation("유저 ID 중복검사")
    @GetMapping(value = "/users/check/id")
    public ResponseEntity<String> duplicationCheckId(@RequestParam("id") String id) {
        JsonObject data = new JsonObject();
        data.addProperty("flag", userService.duplicationCheckId(id));

        return new ResponseEntity<>(data.toString(), HttpStatus.OK);
    }

    @ApiOperation("유저 닉네임 중복검사")
    @GetMapping(value = "/users/check/nickname")
    public ResponseEntity<String> duplicationCheck(@RequestParam("nickname") String nickname) {
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
            tokenService.remove(userId);
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
}