package com.mango.harugomin.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.dto.UserRequestDto;
import com.mango.harugomin.dto.UserResponseDto;
import com.mango.harugomin.dto.UserUpdateRequestDto;
import com.mango.harugomin.dto.UserUpdateResponseDto;
import com.mango.harugomin.jwt.JwtService;
import com.mango.harugomin.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Api(tags = "1. User")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Controller
public class UserController {

    private final UserService userService;
    private final HashtagService hashtagService;
    private final KakaoAPIService kakaoAPIService;
    private final NaverAPIService naverAPIService;
    private final JwtService jwtService;
    private final S3Service s3Service;

    @ApiOperation(value = "index", notes = "연습용 메인 페이지")
    @ApiResponses({
            @ApiResponse(code = 200, message = "200 OK !!"),
            @ApiResponse(code = 500, message = "Internal Server Error !!"),
            @ApiResponse(code = 404, message = "Not Found !!")
    })
    @GetMapping(value = "/")
    public String Hello() {
        return "/index";
    }


    /**
     * 이미지 업로드
     */
    @PostMapping("/uploadImage")
    public String updateProfile(UserRequestDto userRequestDto, MultipartFile file) throws IOException {
        log.info("API updateProfile ! ");
        String imgPath = s3Service.upload(userRequestDto.getProfileImage(), file);

        log.info("image Path : " + imgPath);
        //user.setImageUrl();
        //userService.saveUser();
        return "redirect:/";
    }

    @GetMapping("/uploadImage")
    public String updateProfileImage(Model model) {
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add("https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/0829%2B%ED%9A%8C%EC%9D%98%2B%281%29.txt");
        model.addAttribute("imageList", imgList);
        return "redirect:/";
    }


    /**
     * 닉네임 중복 검사
     */
    @ApiOperation("유저 닉네임 중복검사")
    @GetMapping(value = "/users/check/{nickname}")
    @ResponseBody
    public ResponseEntity<Boolean> duplicationCheck(@PathVariable("nickname") String nickname) {
        boolean nicknameDuplicationCheckStatus = userService.duplicationCheck(nickname);

        return new ResponseEntity<>(nicknameDuplicationCheckStatus, HttpStatus.OK);
    }

    /**
     * 프로필 사진 업데이트
     */
    @ApiOperation("유저 프로필 사진 업데이트")
    @PutMapping(value = "/users/profileImage/{id}")
    @ResponseBody
    public ResponseEntity<UserResponseDto> updateUserProfile(@PathVariable(value = "id") Long userId, MultipartFile file) throws IOException {
        log.info("PUT :: /users/profileImage/{id}");
        User user = userService.findById(userId);

        String imgPath = s3Service.upload(user.getProfileImage(), file);
        user.updateProfileImage(S3Service.CLOUD_FRONT_DOMAIN_NAME + "/" + imgPath);
        userService.saveUser(user);
        log.info("USER profile : " + imgPath);

        return new ResponseEntity<>(new UserResponseDto(user), HttpStatus.OK);
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
    @ResponseBody
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