package com.mango.harugomin.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mango.harugomin.domain.entity.Key;
import com.mango.harugomin.domain.entity.Keys;
import com.mango.harugomin.domain.entity.TokenResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppleUtils {

    @Value("${APPLE.PUBLICKEY.URL}")
    private String APPLE_PUBLIC_KEYS_URL;

    @Value("${APPLE.ISS}")
    private String ISS;

    @Value("${APPLE.AUD}")
    private String AUD;

    @Value("${APPLE.TEAM.ID}")
    private String TEAM_ID;

    @Value("${APPLE.KEY.ID}")
    private String KEY_ID;

    @Value("${APPLE.KEY.PATH}")
    private String KEY_PATH;

    @Value("${APPLE.AUTH.TOKEN.URL}")
    private String AUTH_TOKEN_URL;

    @Value("${APPLE.WEBSITE.URL}")
    private String APPLE_WEBSITE_URL;

    public Map<String, String> getMetaInfo() {

        Map<String, String> metaInfo = new HashMap<>();

        metaInfo.put("CLIENT_ID", AUD);
        metaInfo.put("REDIRECT_URI", APPLE_WEBSITE_URL);
        metaInfo.put("NONCE", "20B20D-0S8-1K8"); // Test value

        return metaInfo;
    }

    public boolean verifyIdentityToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();

            // EXP
            Date currentTime = new Date(System.currentTimeMillis());
            if (!currentTime.before(payload.getExpirationTime())) {
                return false;
            }

            // NONCE(Test value), ISS, AUD
            if (!"20B20D-0S8-1K8".equals(payload.getClaim("nonce")) || !ISS.equals(payload.getIssuer()) || !AUD.equals(payload.getAudience().get(0))) {
                return false;
            }

            // RSA
            if (verifyPublicKey(signedJWT)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean verifyPublicKey(SignedJWT signedJWT) {

        try {
            String publicKeys = HttpClientUtils.doGet(APPLE_PUBLIC_KEYS_URL);
            ObjectMapper objectMapper = new ObjectMapper();
            Keys keys = objectMapper.readValue(publicKeys, Keys.class);
            for (Key key : keys.getKeys()) {
                RSAKey rsaKey = (RSAKey) JWK.parse(objectMapper.writeValueAsString(key));
                RSAPublicKey publicKey = rsaKey.toRSAPublicKey();
                JWSVerifier verifier = new RSASSAVerifier(publicKey);

                if (signedJWT.verify(verifier)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String createClientSecret() {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(KEY_ID).build();
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        Date now = new Date();

        claimsSet.setIssuer(TEAM_ID);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
        claimsSet.setAudience(ISS);
        claimsSet.setSubject(AUD);

        SignedJWT jwt = new SignedJWT(header, claimsSet);

//        try {
//            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey());
//            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
//
//            jwt.sign(jwsSigner);
//
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (JOSEException e) {
//            e.printStackTrace();
//        }

        return jwt.serialize();
    }

    public Payload decodeFromIdToken(String id_token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
            ObjectMapper objectMapper = new ObjectMapper();
            Payload payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), Payload.class);

            if (payload != null) {
                return payload;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public TokenResponse validateAuthorizationGrantCode(String client_secret, String code) {

        Map<String, String> tokenRequest = new HashMap<>();

        tokenRequest.put("client_id", AUD);
        tokenRequest.put("client_secret", client_secret);
        tokenRequest.put("code", code);
        tokenRequest.put("grant_type", "authorization_code");
        tokenRequest.put("redirect_uri", APPLE_WEBSITE_URL);

        return getTokenResponse(tokenRequest);
    }

    public TokenResponse validateAnExistingRefreshToken(String client_secret, String refresh_token) {

        Map<String, String> tokenRequest = new HashMap<>();

        tokenRequest.put("client_id", AUD);
        tokenRequest.put("client_secret", client_secret);
        tokenRequest.put("grant_type", "refresh_token");
        tokenRequest.put("refresh_token", refresh_token);

        return getTokenResponse(tokenRequest);
    }

    private TokenResponse getTokenResponse(Map<String, String> tokenRequest) {

        try {
            String response = HttpClientUtils.doPost(AUTH_TOKEN_URL, tokenRequest);
            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponse tokenResponse = objectMapper.readValue(response, TokenResponse.class);

            if (tokenRequest != null) {
                return tokenResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
