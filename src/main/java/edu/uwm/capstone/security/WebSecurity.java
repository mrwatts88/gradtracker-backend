package edu.uwm.capstone.security;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
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

import java.io.IOException;

import static edu.uwm.capstone.security.SecurityConstants.*;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ProfileDao profileDao;

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
        // TODO in the future we will need to modify this chain and only allow authenticated users access to anyRequest
        http.cors().and().csrf().disable().authorizeRequests()
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

    private void persistDefaultUser(PasswordEncoder pe) {
        try {
            Profile defaultUser = DEFAULT_USER();
            defaultUser.setPassword(pe.encode(defaultUser.getPassword()));

            Profile p = profileDao.readByEmail("admin");
            if (p == null) {
                profileDao.create(defaultUser);
            } else if (!pe.matches(p.getPassword(), pe.encode("admin"))) {
                profileDao.delete(p.getId());
                profileDao.create(defaultUser);
            }
        } catch (JSONException | IOException e) {
            System.err.println("Failed to persist default user");
            e.printStackTrace();
        }
    }

}
