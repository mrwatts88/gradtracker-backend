package edu.uwm.capstone;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("backend-api");
        SpringApplication.run(Application.class, args);
    }
}

@Component
class DataLoader implements ApplicationRunner {

    private ProfileDao profileDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(ProfileDao profileDao, PasswordEncoder passwordEncoder) {
        this.profileDao = profileDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {
        Profile defaultProfile = profileDao.read(1L);

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
