package edu.uwm.capstone.security;

public class SecurityConstants {

    private SecurityConstants() {
    } // removes sonarqube code smell

    static final String SECRET = "SecretKeyToGenJWTs"; // TODO do not hard code this secret
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    public static final String AUTHENTICATE_URL = "/auth";

}
