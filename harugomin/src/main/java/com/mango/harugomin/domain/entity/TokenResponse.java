package com.mango.harugomin.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenResponse {
    private String access_token;
    private Long expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
}
