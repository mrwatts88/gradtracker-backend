package edu.uwm.capstone.security;

public class SecurityConstants {

    static final String SECRET = "SecretKeyToGenJWTs"; // TODO do not hard code this secret
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    //    public static final String SIGN_UP_URL = "/signup";
    public static final String AUTHENTICATE_URL = "/auth";

    /* custom JWT claim names for User authentication */
    public static final String JWT_CLAIM_ID = "id";
    public static final String JWT_CLAIM_FIRST_NAME = "first name";
    public static final String JWT_CLAIM_LAST_NAME = "last name";
    public static final String JWT_CLAIM_PANTHER_ID = "panther id";
    public static final String JWT_CLAIM_EMAIL = "email";

    /* default user information */
    public static final String DEFAULT_USER_FIRST_NAME = "default_first_name";
    public static final String DEFAULT_USER_LAST_NAME = "default_last_name";
    public static final String DEFAULT_USER_EMAIL = "default@uwm.edu";
    public static final String DEFAULT_USER_PANTHER_ID = "123456789";
    public static final String DEFAULT_USER_PASSWORD = "password";

    public static final String DEFAULT_USER_CREDENTIALS = "{ \"email\": \"" + DEFAULT_USER_EMAIL + "\", \"password\": \"" + DEFAULT_USER_PASSWORD + "\" }";

}
