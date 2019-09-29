package edu.uwm.capstone;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ApplicationConfigUnitTest {

    private ApplicationConfig applicationConfig;

    @Before
    public void setUp() {
        applicationConfig = new ApplicationConfig();
    }

    @Test
    public void restTemplate() {
        assertNotNull(applicationConfig.restTemplate());
    }

}
