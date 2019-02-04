package edu.uwm.capstone.swagger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwaggerControllerUnitTest {

    private SwaggerController swaggerController;

    @Before
    public void setUp() {
        swaggerController = new SwaggerController();
    }

    @Test
    public void swaggerui() {
        assertEquals(SwaggerController.CONTROLLER_DOCS, swaggerController.swaggerui());
    }

}
