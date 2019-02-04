package edu.uwm.capstone.swagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uwm.capstone.helper.TestHelper;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class SwaggerConfigUnitTest {

    private SwaggerConfig swaggerConfig;

    @Before
    public void setUp() {
        swaggerConfig = new SwaggerConfig();
    }

    /**
     * Verify that the following methods are working correctly:
     * <ul>
     *     <li>{@link SwaggerConfig#setDescription}</li>
     *     <li>{@link SwaggerConfig#setTitle}</li>
     *     <li>{@link SwaggerConfig#setVersion}</li>
     *     <li>{@link SwaggerConfig#setHost}</li>
     *     <li>{@link SwaggerConfig#swaggerSpringMvcPlugin}</li>
     * </ul>
     */
    @Test
    public void swaggerSpringMvcPlugin() {
        String description = TestHelper.randomAlphabetic(100);
        String title = TestHelper.randomAlphabetic(80);
        String version = TestHelper.randomAlphabetic(60);
        String host = TestHelper.randomAlphabetic(40);

        swaggerConfig.setDescription(description);
        swaggerConfig.setTitle(title);
        swaggerConfig.setVersion(version);
        swaggerConfig.setHost(host);

        assertNotNull(swaggerConfig.swaggerSpringMvcPlugin());
    }

}
