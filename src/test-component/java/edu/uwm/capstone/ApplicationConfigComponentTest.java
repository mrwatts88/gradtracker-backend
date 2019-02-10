package edu.uwm.capstone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import edu.uwm.capstone.util.Concatenation;
import static org.junit.Assert.assertNotNull;

/**
 * This test class exercises the spring boot based {@link Application} running in memory to verify that the bean
 * instantiated within {@link ApplicationConfig#restTemplate()} is correctly available to the running application.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UnitTestConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ApplicationConfigComponentTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Concatenation concatenation;

    @Test
    public void verify() throws Exception {
        assertNotNull("RestTemplate", restTemplate);
        assertNotNull("Concatenation", concatenation);
    }

}
