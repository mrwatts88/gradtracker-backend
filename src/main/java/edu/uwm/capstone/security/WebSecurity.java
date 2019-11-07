package edu.uwm.capstone.security;

import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static edu.uwm.capstone.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                .antMatchers(HttpMethod.GET, "/swaggerui", "/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources", "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**").permitAll()
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

        persistDefaultRole();
        persistDefaultUser(pe);

        auth.userDetailsService(userDetailsService())
                .passwordEncoder(pe);

    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    private void persistDefaultUser(PasswordEncoder passwordEncoder) {
        User defaultUser = userDao.readByEmail(DEFAULT_USER.getEmail());

        if (defaultUser != null) return;

        LOGGER.info("Persisting default user {}", DEFAULT_USER);
        DEFAULT_USER.setPassword(passwordEncoder.encode(DEFAULT_USER.getPassword()));
        DEFAULT_USER = userDao.create(DEFAULT_USER);
    }

    private void persistDefaultRole() {
        Role defaultRole = roleDao.readByName(DEFAULT_ROLE.getName());

        if (defaultRole != null) return;

        LOGGER.info("Persisting default user {}", DEFAULT_ROLE);
        roleDao.create(DEFAULT_ROLE);
    }
}
