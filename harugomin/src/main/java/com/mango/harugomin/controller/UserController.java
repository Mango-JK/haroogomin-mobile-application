package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.jwt.JwtService;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
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

    /**
     * @param id
     * @return UserInfo
     */
    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> findOne(@PathVariable("id") long id) {
        return new ResponseEntity<User>(userService.findById(id), HttpStatus.OK);
    }

    //
    //
    @GetMapping(value = "/user/login/kakao")
    @ApiOperation("카카오 코드 발급받기")
    public String getKakaoCode(@RequestParam("code") String code) {
        log.info("User Kakao Code : " + code);

        ResponseEntity<String> AccessToken = kakaoAPIService.getAccessToken(code);

        log.info("My AccessToken : " + AccessToken);
        return "index";
    }

    @GetMapping(value = "/user/login/naver")
    @ApiOperation("네이버 코드 발급받기")
    public String getNaverCode(@RequestParam(value = "code") String code,
                               @RequestParam(value = "state") String state) {
        log.info("User Naver Code : " + code);
        log.info("State Code : " + state);

        ResponseEntity<String> AccessToken = naverAPIService.getAccessToken(code, state);

        log.info("Naver AccessToken : " + AccessToken);
        return "index";
    }

    //
    //

    /**
     * @param accessToken
     * @return UserJWTToken
     */
    @PostMapping("/user/login/kakao")
    @ApiOperation("카카오 로그인")
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
     * @param jwtToken
     * @return UserInfo
     */
    @PostMapping("/user/check")
    @ApiOperation("토큰 검증")
    public Object checkToken(@RequestParam String jwtToken) {
        log.info("UserController : checkToken");

        Object result = null;

        if (jwtService.isUsable(jwtToken)) {
            result = jwtService.get("user");
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}