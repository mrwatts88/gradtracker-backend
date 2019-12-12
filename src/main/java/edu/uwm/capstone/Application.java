package edu.uwm.capstone;

import edu.uwm.capstone.helper.DefaultEntities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        Thread.currentThread().setName("backend-api");
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);

        context.getBean(DefaultEntities.class).persistDefaultEntities();
    }

}
