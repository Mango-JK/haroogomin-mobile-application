package com.mango.harugomin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mango.harugomin.domain.entity.TokenResponse;
import com.mango.harugomin.domain.entity.User;
import com.mango.harugomin.dto.UserRequestDto;
import com.mango.harugomin.jwt.JwtService;
import com.mango.harugomin.utils.AppleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleAPIService {
    private final AppleUtils appleUtils;

    public String getAppleClientSecret(String id_token) {
        log.info("##### getAppleClientSecret START #####");
        if (appleUtils.verifyIdentityToken(id_token)) {
            log.info("????????????????");
            return appleUtils.createClientSecret();
        }

        return null;
    }

    public String getPayload(String id_token) {
        log.info("ID값 : " + appleUtils.decodeFromIdToken(id_token).getSub());
        return appleUtils.decodeFromIdToken(id_token).toString();
    }

    public String getUserId(String id_token) {
        return appleUtils.decodeFromIdToken(id_token).getSub();
    }

    public TokenResponse requestCodeValidations(String client_secret, String code, String refresh_token) throws IOException {
        log.info(":: START requestCodeValidations :: ");
        TokenResponse tokenResponse = new TokenResponse();

        if (client_secret != null && code != null && refresh_token == null) {
            log.info("====== validateAuthorizationGrantCode =========");
            tokenResponse = appleUtils.validateAuthorizationGrantCode(client_secret, code);
        } else if (client_secret != null && code == null && refresh_token != null) {
            log.info("========= validateAnExistingRefreshToken ============");
            tokenResponse = appleUtils.validateAnExistingRefreshToken(client_secret, refresh_token);
        }

        return tokenResponse;
    }

}
