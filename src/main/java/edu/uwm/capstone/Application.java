package edu.uwm.capstone;

import edu.uwm.capstone.db.ProfileDao;
import edu.uwm.capstone.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

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

    @Autowired
    public DataLoader(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    public void run(ApplicationArguments args) {
        final Profile defaultProfile = Profile.builder()
                .firstName("default_first_name")
                .lastName("default_last_name")
                .email("default@uwm.edu")
                .pantherId("12345678")
                .password("password")
                .build();

        profileDao.create(defaultProfile);
    }
}
