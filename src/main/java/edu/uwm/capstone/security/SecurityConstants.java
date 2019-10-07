package edu.uwm.capstone.security;

public class SecurityConstants {
    static final String SECRET = "SecretKeyToGenJWTs"; // TODO do not hard code this secret
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
//    public static final String SIGN_UP_URL = "/signup";
    public static final String AUTHENTICATE_URL = "/auth";

    public static final String DEFAULT_USER_CREDENTIALS = "{ \"email\": \"default@uwm.edu\", \"password\": \"password\" }";
}