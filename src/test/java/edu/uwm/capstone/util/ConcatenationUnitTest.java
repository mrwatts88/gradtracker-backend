package edu.uwm.capstone.util;

import org.junit.Test;

import edu.uwm.capstone.helper.TestHelper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConcatenationUnitTest {

    /**
     * Verify that {@link Concatenation#concatenate} correctly concatenates "one" and "two" into "onetwo".
     */
    @Test
    public void concatenate() {
        Concatenation concatenation = new Concatenation();
        String result = concatenation.concatenate("one", "two");
        assertEquals("onetwo", result);
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
        Concatenation concatenation = new Concatenation();

        Integer repeatTheTest = TestHelper.randomInteger(50, 150);
        Integer firstStringLength;
        Integer secondStringLength;
        Integer concatenatedStringLength;
        String firstString;
        String secondString;
        String concatenatedString;

        for (int i=0; i <= repeatTheTest; i++) {
            // get random sizes for our two strings
            firstStringLength = TestHelper.randomInteger(25, 100);
            secondStringLength = TestHelper.randomInteger(25, 100);
            concatenatedStringLength = firstStringLength + secondStringLength;

            // get random values based on the random sizes for our two strings
            firstString = TestHelper.randomAlphabetic(firstStringLength);
            secondString = TestHelper.randomAlphanumeric(secondStringLength);

            // concatenate our two randomly sized random value strings
            concatenatedString = concatenation.concatenate(firstString, secondString);

            // verify that the concatenated string length
            assertEquals("Invalid length of the concatenated string",
                    concatenatedStringLength.intValue(), concatenatedString.length());

            // verify that the concatenated string begins with the first string and ends with the second string
            assertTrue(concatenatedString.startsWith(firstString));
            assertTrue(concatenatedString.endsWith(secondString));
        }
    }

}
