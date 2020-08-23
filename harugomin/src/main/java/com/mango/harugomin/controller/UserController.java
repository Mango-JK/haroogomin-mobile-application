package com.mango.harugomin.controller;

import com.mango.harugomin.service.KakaoAPIService;
import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "{1. User}")
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {
    private final UserService userService;
    private final KakaoAPIService kakaoAPIService;

    @ApiOperation(value = "index", notes = "연습용 메인 페이지")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/")
    public String Hello(){
        return "index";
    }

    @GetMapping(value = "/user/login/kakao")
    @ApiOperation("카카오 코드 발급받기")
    public String getKakaoCode(@RequestParam("code") String code){
        log.info("User Kakao Code : " + code);

        ResponseEntity<String> AccessToken = kakaoAPIService.getAccessToken(code);

        return "index";
    }

//    @PostMapping("/user/login/kakao")
//    @ApiOperation("카카오 로그인")
//    public String kakaoLogin(@RequestParam String accessToken) {
//        log.info("POST : /user/login/kakao");
//        log.info("AccessToken : " + accessToken);
//
//        JsonNode json = kakaoAPIService.getKaKaoUserInfo(accessToken);
//
//        long id = json.get("id").asLong();
//        if(userService.findById(id) == null) {
//            return String.valueOf(id);
//        }
//
//        String result = null;
//        try {
//            result = kakaoAPIService.redirectToken(json); // 토큰 발행
//        } catch (Exception e) {
//            log.error(e + "");
//        }
//        return result;
//    }
}
