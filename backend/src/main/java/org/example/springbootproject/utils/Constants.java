package org.example.springbootproject.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Constants {

    private Constants(){}

    public static final int STRING_MAX_LENGTH = 255;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final long MAX_AGE_SECS = 3600;
    public static int PAGE_SIZE;
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String DIGITS = "0123456789";
    public static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
    public static final Map<String, String> ALLOW_ENDPOINTS = new HashMap<>();
    public static String SECRET_KEY;
    public static long TOKEN_EXPIRE_TIME;
    public static String FRONT_END_URL = "http://localhost:9999";
    public static long REFRESH_TOKEN_EXPIRE_TIME;
    public static String SYSTEM_EMAIL;
    public static int OTP_COOKIE_EXPIRE_TIME = 600;
    public static int OTP_LENGTH = 6;
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    public static final int MAX_SIZE_NUMBER = 100000000;
    public static final int MIN_SIZE_NUMBER = 1;
    public static final int MAX_PRICE = 100000000;
    public static final int ROOM_STATUS_VACANT = 0;
    public static final int ROOM_STATUS_RENT = 1;
    public static final int ROOM_STATUS_REPAIR = 2;

    @Value("${app.secret_key}")
    private String secretKey;

    @Value("${app.token_expire_time:1800000}")
    private long tokenExpireTime;

    @Value("${app.front_end_url}")
    private String frontEndUrl;

    @Value("${jwt.refresh_token_expire:86400000}")
    private long refreshTokenExpireTime;

    @Value("${app.system_email}")
    private String SystemEmail;

    @Value("${app.page_size}")
    private int pageSize;

    @PostConstruct
    private void init() {
        SECRET_KEY = secretKey;
        TOKEN_EXPIRE_TIME = tokenExpireTime;
        FRONT_END_URL = frontEndUrl;
        REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
        SYSTEM_EMAIL = SystemEmail;
        PAGE_SIZE = pageSize;
        ALLOW_ENDPOINTS.put("AUTH_API", "api/auth/**");
        ALLOW_ENDPOINTS.put("SWAGGER", "swagger-ui/**");
        ALLOW_ENDPOINTS.put("API_DOCS", "v3/api-docs/**");
    }
}
