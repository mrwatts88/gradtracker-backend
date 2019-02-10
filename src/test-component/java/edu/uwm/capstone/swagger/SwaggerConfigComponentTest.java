package edu.uwm.capstone.swagger;

import edu.uwm.capstone.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.Assert.assertNotNull;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that the bean
 * instantiated within {@link SwaggerConfig#swaggerSpringMvcPlugin} is correctly available to the running application.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SwaggerConfigComponentTest {

    @Autowired
    Docket swaggerSpringMvcPlugin;

    @Test
    public void verify() {
        assertNotNull("Docket", swaggerSpringMvcPlugin);
    }

}
