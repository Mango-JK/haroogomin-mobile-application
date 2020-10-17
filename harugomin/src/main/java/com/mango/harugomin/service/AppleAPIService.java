package com.mango.harugomin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.dto.UserRequestDto;
import com.mango.harugomin.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleAPIService {
    private final UserService userService;
    private final JwtService jwtService;

    public JsonNode getAppleUserInfo(String accessToken) {
        String requestURL = "https://openapi.naver.com/v1/nid/me";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(requestURL);

        post.addHeader("Authorization", "Bearer " + accessToken);
        JsonNode returnNode = null;

        HttpResponse response;
        try {
            response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode;
    }

    @Transactional
    public String redirectToken(JsonNode json) {
        long id = json.get("response").get("id").asLong();
        String nickname = json.get("response").get("nickname").toString();
        nickname = nickname.substring(1, nickname.length() - 1);
        String ageRange = "0";

        User user = null;
        int random = (int) Math.round(Math.random() * 4) + 1;
        String image = "https://hago-storage-bucket.s3.ap-northeast-2.amazonaws.com/default0" + random + ".jpg";
        if (userService.findById(id).isEmpty()) {
            User newUser = User.builder()
                    .userId(id)
                    .nickname(nickname)
                    .profileImage(image)
                    .ageRange(Integer.parseInt(ageRange))
                    .build();

            user = userService.saveUser(newUser);
        } else {
            user = userService.findById(id).get();
        }
        UserRequestDto userResponseDto = new UserRequestDto(user);
        String jwt = jwtService.create("user", userResponseDto, "user");

        return jwt;
    }

    public String getAppleClientSecret(String id_token) {
        if (appleUtils.verifyIdentityToken(id_token)) {
            return appleUtils.createClientSecret();
        }

        return null;
    }
}
