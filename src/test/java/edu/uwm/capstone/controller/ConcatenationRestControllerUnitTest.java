package edu.uwm.capstone.controller;

import org.junit.Before;
import org.junit.Test;

import edu.uwm.capstone.helper.TestHelper;
import edu.uwm.capstone.util.Concatenation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConcatenationRestControllerUnitTest {

    private ConcatenationRestController concatenationRestController;

    @Before
    public void setUp() {
        concatenationRestController = new ConcatenationRestController();
    }

    /**
     * Verify that {@link ConcatenationRestController#concatenate} correctly concatenates "one" and "two" into "onetwo".
     */
    @Test
    public void concatenate() {
        //String result = concatenationRestController.concatenate("one", "two", new MockHttpServletResponse());
        String response = concatenationRestController.concatenate("one", "two");
        assertEquals("onetwo", response);
    }

    /**
     * This test uses our {@link TestHelper} to get random string values for use in verifying that
     * {@link Concatenation#concatenate} correctly concatenates the values that are provided.
     * The test is performed a random number of times based on the repeatTheTest provided by
     * {@link TestHelper#randomInteger} and uses random strings provided by {@link TestHelper#randomAlphabetic}
     * and {@link TestHelper#randomAlphanumeric} which introduce randomness into the validations that are performed.
     */
    @Test
    public void verifyRandomConcatenations() {
        Integer repeatTheTest = TestHelper.randomInteger(20, 80);
        Integer firstStringLength;
        Integer secondStringLength;
        Integer concatenatedStringLength;
        String firstString;
        String secondString;
        String response;

        for (int i=0; i <= repeatTheTest; i++) {
            // get random sizes for our two strings
            firstStringLength = TestHelper.randomInteger(40, 120);
            secondStringLength = TestHelper.randomInteger(40, 120);
            concatenatedStringLength = firstStringLength + secondStringLength;

            // get random values based on the random sizes for our two strings
            firstString = TestHelper.randomAlphabetic(firstStringLength);
            secondString = TestHelper.randomAlphanumeric(secondStringLength);

            // concatenate our two randomly sized random value strings
            response = concatenationRestController.concatenate(firstString, secondString);

            // verify that the concatenated string length
            assertEquals("Invalid length of the concatenated string",
                    concatenatedStringLength.intValue(), response.length());

            // verify that the concatenated string begins with the first string and ends with the second string
            assertTrue(response.startsWith(firstString));
            assertTrue(response.endsWith(secondString));
        }
    }

}
