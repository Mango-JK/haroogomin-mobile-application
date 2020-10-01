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
import java.util.StringTokenizer;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverAPIService {

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
        log.info("NaverAPIService :: getNaverUserInfo");

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

        log.info(returnNode.toString());
        return returnNode;
    }

    @Transactional
    public String redirectToken(JsonNode json) {
        log.info("NaverAPIService :: redirectToken");

        long id = json.get("response").get("id").asLong();
        String nickname = json.get("response").get("nickname").toString();
        nickname = nickname.substring(1, nickname.length() - 1);
        String profileImage = json.get("response").get("profile_image").toString();
        profileImage = profileImage.substring(1, profileImage.length() - 1);

        String age = json.get("response").get("age").toString();
        age = age.substring(1, age.length() - 1);
        StringTokenizer stringTokenizer = new StringTokenizer(age, "-");
        String ageRange = stringTokenizer.nextToken();

        User user = userService.findById(id);

        if (user == null) {
            User newUser = User.builder()
                    .userId(id)
                    .ageRange(Integer.parseInt(ageRange))
                    .point(0)
                    .build();

            user = userService.saveUser(newUser);
        }

        user.update(nickname, profileImage);

        UserRequestDto userResponseDto = new UserRequestDto(user);
        String jwt = jwtService.create("user", userResponseDto, "user");

        return jwt;
    }
}
