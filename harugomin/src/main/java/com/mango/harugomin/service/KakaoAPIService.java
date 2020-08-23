package com.mango.harugomin.service;

import com.mango.harugomin.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.http.HttpResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoAPIService {

    private final UserService userService;
    private final JwtService jwtService;

    private final String requestURL = "https://kapi.kakao.com/v2/user/me";
    private final String AUTH_HOST = "https://kauth.kakao.com";

    public ResponseEntity<String> getAccessToken(String code) {
        log.info("KAKAO API SERVICE :: Start getAccessToken -> CODE : " + code);

        final String tokenRequestUrl = AUTH_HOST + "/oauth/token";

        String CLIENT_ID = "7a888c52e90c278c82e7da483c93375f"; // 해당 앱의 REST API KEY 정보. 개발자 웹사이트의 대쉬보드에서 확인 가능
        String REDIRECT_URI = "http://localhost:8080/api/user/login/kakao"; // 해당 앱의 설정된 uri. 개발자 웹사이트의 대쉬보드에서 확인 및 설정 가능

        HttpsURLConnection conn = null;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;
        final StringBuffer buffer = new StringBuffer();

        try {
            final String params = String.format("grant_type=authorization_code&client_id=%s&redirect_uri=%s&code=%s",
                    CLIENT_ID, REDIRECT_URI, code);

            final URL url = new URL(tokenRequestUrl);

            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(params);
            writer.flush();

            final int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + tokenRequestUrl);
            System.out.println("Post parameters : " + params);
            System.out.println("Response Code : " + responseCode);

            isr = new InputStreamReader(conn.getInputStream());
            reader = new BufferedReader(isr);

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            System.out.println(buffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception ignore) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception ignore) {
                }
            }
        }
        return new ResponseEntity<String>(buffer.toString(), HttpStatus.OK);
    }

//    public JsonNode getKaKaoUserInfo(String access_Token) {
//        log.info("KakaoAPIService : getKaKaoUserInfo");
//        final HttpClient client = HttpClientBuilder.create().build();
//        final HttpPost post = new HttpPost(requestURL);
//
//        post.addHeader("Authorization", "Bearer " + access_Token);
//        JsonNode returnNode = null;
//
//        HttpResponse response;
//        try {
//            response = client.execute(post);
//            ObjectMapper mapper = new ObjectMapper();
//            returnNode = mapper.readTree(response.getEntity().getContent());
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return returnNode;
//    }
//
//    @Transactional
//    public String redirectToken(JsonNode json) {
//        log.info("KakaoAPIService : redirectToken");
//
//        long num = json.get("id").asLong();
//        String nickname = json.get("kakao_account").get("profile").get("nickname").toString();
//        nickname = nickname.substring(1, nickname.length() - 1);
//        String picture = null;
//        if (json.get("kakao_account").get("profile").has("thumbnail_image_url")) {
//            picture = json.get("kakao_account").get("profile").get("thumbnail_image_url").toString();
//            picture = picture.substring(1, picture.length() - 1);
//            String temp = picture.substring(0, 4);
//            String temp2 = picture.substring(4, picture.length());
//            picture = temp + "s" + temp2; // https 작업
//        }
//
//        User user = userService.findByNum(num);
//        user.update(nickname, picture);
//
//        UserResponseDto userResponseDto = new UserResponseDto(user);
//
//        String jwt = jwtService.create("user", userResponseDto, "user");
//
//        return jwt;
//    }
}
