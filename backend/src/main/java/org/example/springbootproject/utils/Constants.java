package org.example.springbootproject.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    private Constants(){}

    public static final int STRING_MAX_LENGTH = 255;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int PASSWORD_MIN_LENGTH = 8;

    public static final String ALLOW_ENDPOINT= "api/auth/**";
    public static String SECRET_KEY;
    public static long TOKEN_EXPIRE_TIME;
    public static String FRONT_END_URL = "http://localhost:9999";
    public static long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${app.secret_key}")
    private String secretKey;

    @Value("${app.token_expire_time:1800000}")
    private long tokenExpireTime;

    @Value("${app.front_end_url}")
    private String frontEndUrl;

    @Value("${jwt.refresh_token_expire:86400000}")
    private long refreshTokenExpireTime;

    @PostConstruct
    private void init() {
        SECRET_KEY = secretKey;
        TOKEN_EXPIRE_TIME = tokenExpireTime;
        FRONT_END_URL = frontEndUrl;
        REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
    }
}
