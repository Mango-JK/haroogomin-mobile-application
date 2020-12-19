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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverApiService {

    private final UserService userService;
    private final JwtService jwtService;

    public ResponseEntity<String> getAccessToken(String code, String state) {
        String clientId = "k9f8GUK0cmsRiCPbJeoc";
        String naverClientSecret = "pgFF2izLEz";

        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + naverClientSecret;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        StringBuffer res = new StringBuffer();
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            final int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }

    public JsonNode getNaverUserInfo(String accessToken) {
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
        String nickname = "naverUser";
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

            user = userService.save(newUser);
        } else {
            user = userService.findById(id).get();
        }
        UserRequestDto userResponseDto = new UserRequestDto(user);
        String jwt = jwtService.create("user", userResponseDto, "user");

        return jwt;
    }
}