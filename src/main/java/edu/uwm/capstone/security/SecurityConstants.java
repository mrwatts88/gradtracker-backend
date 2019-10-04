package edu.uwm.capstone.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.model.Profile;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs"; // TODO do not hard code this secret
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/signup";
    public static final String AUTHENTICATE_URL = "/authenticate";

    // default user
    public static final String DEFAULT_USER_EMAIL = "admin";
    public static final String DEFAULT_USER_PASSWORD = "admin";
    public static Profile DEFAULT_USER() throws JSONException, IOException {
        return new ObjectMapper()
                .readValue(new JSONObject()
                        .put("firstName", "admin")
                        .put("lastName", "admin")
                        .put("email", DEFAULT_USER_EMAIL)
                        .put("pantherId", "admin")
                        .put("password", DEFAULT_USER_PASSWORD)
                        .toString(), Profile.class);
    }
}