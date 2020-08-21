package com.mango.harugomin.controller;

import com.mango.harugomin.service.KakaoAPI;
import com.mango.harugomin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "{1. User}")
@RequiredArgsConstructor
@RequestMapping("/v1")
@RestController
public class UserController {
    private final UserService userService;
    private final KakaoAPI kakaoAPI;

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

    @GetMapping(value = "/login")
    public String login() {
        System.out.println("???????????");
        return "index";
    }

    @GetMapping(value = "/user/login")
    public String kakaoLogin(@RequestParam("code") String code) {
        System.out.println("request access code : " + code);
        String access_Token = kakaoAPI.getAccessToken(code);
        System.out.println("controller access_token : " + access_Token);
        return "index";
    }

}
