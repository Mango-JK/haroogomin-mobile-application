package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.jwt.JwtService;
import com.mango.harugomin.service.HashtagService;
import com.mango.harugomin.service.KakaoAPIService;
import com.mango.harugomin.service.NaverAPIService;
import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;
    private final HashtagService hashtagService;
    private final KakaoAPIService kakaoAPIService;
    private final NaverAPIService naverAPIService;
    private final JwtService jwtService;

    @ApiOperation(value = "index", notes = "연습용 메인 페이지")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/")
    public String Hello() {
        return "index";
    }












    @ApiOperation("유저 해시태그 업데이트")
    @PostMapping(value = "/users/hashtag/{userId}")
    public ResponseEntity<Long> updateUserHashtag(@PathVariable("userId") Long userId, @RequestParam("hashtag") String hashtag) {
        if(userService.updateUserHashtag(userId, hashtag) > 0 ) {
            User user = userService.findById(userId);
            hashtagService.countUp(user.getUserHashtag().getTagId());
            return new ResponseEntity<Long>(userId, HttpStatus.OK);
        }
        return new ResponseEntity<Long>(-1L, HttpStatus.BAD_REQUEST);
    }

    @ApiOperation("카카오 코드 발급받기")
    @GetMapping(value = "/users/login/kakao")
    public String getKakaoCode(@RequestParam("code") String code) {
        log.info("User Kakao Code : " + code);

        ResponseEntity<String> AccessToken = kakaoAPIService.getAccessToken(code);

        log.info("My AccessToken : " + AccessToken);
        return "index";
    }


    @ApiOperation("네이버 코드 발급받기")
    @GetMapping(value = "/users/login/naver")
    public String getNaverCode(@RequestParam(value = "code") String code,
                               @RequestParam(value = "state") String state) {
        log.info("User Naver Code : " + code);
        log.info("State Code : " + state);

        ResponseEntity<String> AccessToken = naverAPIService.getAccessToken(code, state);

        log.info("Naver AccessToken : " + AccessToken);
        return "index";
    }

    /**
     * @param accessToken
     * @return UserJWTToken
     */
    @ApiOperation("카카오 로그인")
    @PostMapping("/users/login/kakao")
    public String kakaoLogin(@RequestParam String accessToken) {
        log.info("POST :: /user/login/kakao");

        JsonNode json = kakaoAPIService.getKaKaoUserInfo(accessToken);

        String result = null;
        try {
            result = kakaoAPIService.redirectToken(json); // 토큰 발행
        } catch (Exception e) {
            log.error(e + "");
        }

        return result;
    }

    /**
     *
     * @param accessToken
     * @return UserJWTToken
     */
    @ApiOperation("네이버 로그인")
    @PostMapping("/users/login/naver")
    public String naverLogin(@RequestParam String accessToken) {
        log.info("POST :: /user/login/naver");

        JsonNode json = naverAPIService.getNaverUserInfo(accessToken);

        String result = null;
        try {
            result = naverAPIService.redirectToken(json);
        } catch (Exception e) {
            log.error(e + "");
        }

        return result;
    }

    /**
     * @param jwtToken
     * @return UserInfo
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
}