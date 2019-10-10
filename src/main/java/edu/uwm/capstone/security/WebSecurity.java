package edu.uwm.capstone.security;

import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static edu.uwm.capstone.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurity.class);

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring security chain");
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger**").permitAll()
                .antMatchers(HttpMethod.POST, AUTHENTICATE_URL).permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilter(new JWTAuthenticationFilter(AUTHENTICATE_URL, authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        PasswordEncoder pe = passwordEncoder();

        persistDefaultUser(pe);

        auth.userDetailsService(userDetailsService())
                .passwordEncoder(pe);

    }

    @Autowired
    private UserDao userDao;

    private void persistDefaultUser(PasswordEncoder passwordEncoder) {
        User defaultUser = userDao.readByEmail(DEFAULT_USER_EMAIL);

        if (defaultUser != null) return;

        defaultUser = User.builder()
                .firstName(DEFAULT_USER_FIRST_NAME)
                .lastName(DEFAULT_USER_LAST_NAME)
                .email(DEFAULT_USER_EMAIL)
                .pantherId(DEFAULT_USER_PANTHER_ID)
                .password(passwordEncoder.encode(DEFAULT_USER_PASSWORD))
                .build();

        LOGGER.info("Persisting default user {}", defaultUser);
        userDao.create(defaultUser);
    }
}
