package edu.uwm.capstone.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.model.UserLoginRequest;
import edu.uwm.capstone.security.exception.JWTAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static edu.uwm.capstone.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private ObjectMapper jwtUserSubjectMapper;
    private ObjectMapper userCredentialsObjectMapper;

    public JWTAuthenticationFilter(String processUrl, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(processUrl);

        // create object mapper with mixin
        jwtUserSubjectMapper = new ObjectMapper().addMixIn(User.class, User.UserMixIn.class);

        // set filter provider to JWTFilter provider
        jwtUserSubjectMapper.setFilterProvider(new SimpleFilterProvider().addFilter("JWTFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept("firstName", "lastName", "pantherId", "email", "enabled", "roleNames", "authorities")));

        userCredentialsObjectMapper = new ObjectMapper();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {
        try {
            UserLoginRequest userCredentials = userCredentialsObjectMapper.readValue(req.getInputStream(), UserLoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getEmail(),
                            userCredentials.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new JWTAuthenticationException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        User user = (User) auth.getPrincipal();

        String token = JWT.create()
                .withSubject(jwtUserSubjectMapper.writeValueAsString(user))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}