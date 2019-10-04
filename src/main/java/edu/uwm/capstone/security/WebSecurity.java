package edu.uwm.capstone.security;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

import static edu.uwm.capstone.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO swaggerui is inaccessible unless authenticated
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/swagger**").permitAll()
                .antMatchers(HttpMethod.POST, AUTHENTICATE_URL).permitAll()
                .anyRequest().authenticated()
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
    private ProfileDao profileDao;

    private void persistDefaultUser(PasswordEncoder passwordEncoder) {
        Profile defaultProfile = profileDao.readByEmail("default@uwm.edu");

        if(defaultProfile != null) {
            return;
        }

        defaultProfile = Profile.builder()
                .firstName("default_first_name")
                .lastName("default_last_name")
                .email("default@uwm.edu")
                .pantherId("123456789")
                .password(passwordEncoder.encode("password"))
                .build();

        profileDao.create(defaultProfile);
    }

}
