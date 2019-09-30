package edu.uwm.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("backend-api");
        SpringApplication.run(Application.class, args);
    }

}
