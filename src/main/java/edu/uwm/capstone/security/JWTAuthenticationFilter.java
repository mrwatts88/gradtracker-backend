package edu.uwm.capstone.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.model.UserLoginRequest;
import edu.uwm.capstone.security.exception.JWTAuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static edu.uwm.capstone.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(String processUrl, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(processUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) {
        try {
            UserLoginRequest creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
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
                                            Authentication auth) throws IOException, ServletException {

        User user = (User) auth.getPrincipal();

//        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter("jwtFilter", SimpleBeanPropertyFilter.filterOutAllExcept("createdDate", "updateDate", "password"));
//        filters.setFailOnUnknownId(false);

        // TODO

//            FilterProvider filters = new SimpleFilterProvider()
//                    .addFilter("filterDatesAndPassword", SimpleBeanPropertyFilter.serializeAllExcept("createdDate", "updateDate", "password"));

            ObjectMapper om = new ObjectMapper();
//            om.setFilters(filters);

            String token = JWT.create()
                    .withSubject(om.writeValueAsString(user))
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(HMAC512(SECRET.getBytes()));

//            System.out.println(token);

            res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

    }
}