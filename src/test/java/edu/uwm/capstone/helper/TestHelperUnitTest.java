package edu.uwm.capstone.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import edu.uwm.capstone.model.cards.Rank;
import edu.uwm.capstone.model.cards.Suit;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import static org.junit.Assert.*;

public class TestHelperUnitTest {

    /**
     * {@link TestHelper} is a utility class and as such it should only contains static methods.
     * This test verifies that the {@link TestHelper} object only contains a private constructor
     * so that all of its methods will be provided statically.
     */
    @Test
    public void verifyConstructorIsPrivate() throws Exception {
        Constructor<TestHelper> constructor = TestHelper.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * {@link TestHelper} is a utility class and as such it should only contains static methods.
     * This test verifies that the {@link TestHelper} object does not contain any public constructors
     * so that all of its methods will be provided statically.
     */
    @Test
    public void verifyOnlyPrivateConstructors() {
        assertEquals(0, TestHelper.class.getConstructors().length);
    }

    /**
     * Verify that {@link TestHelper#randomLong} creates a random {@link Long}.
     */
    @Test
    public void randomLong() {
        assertNotNull(TestHelper.randomLong());
        assertTrue(TestHelper.randomLong() instanceof Long);
    }

    /**
     * Verify that {@link TestHelper#randomDouble} creates a random {@link Double}.
     */
    @Test
    public void randomDouble() {
        assertNotNull(TestHelper.randomDouble());
        assertTrue(TestHelper.randomDouble() instanceof Double);
    }

    /**
     * Verify that {@link TestHelper#randomInteger} creates a random {@link Integer}.
     */
    @Test
    public void randomInteger() {
        TestHelper.randomInteger();
        assertTrue(TestHelper.randomInteger() instanceof Integer);
    }

    /**
     * Verify that {@link TestHelper#randomInteger} creates a random {@link Integer}
     * within the min and max boundaries that is provided.
     */
    @Test
    public void randomIntWithStartStop() {
        int min = 3;
        int max = 1111;
        int randomInt = TestHelper.randomInteger(min, max);
        assertTrue(randomInt >= min);
        assertTrue(randomInt <= max);
        assertTrue(TestHelper.randomInteger(min, max) instanceof Integer);
    }

    /**
     * Verify that {@link TestHelper#randomBigDecimal} creates a random {@link BigDecimal}.
     */
    @Test
    public void randomBigDecimal() {
        assertNotNull(TestHelper.randomBigDecimal());
        assertTrue(TestHelper.randomBigDecimal() instanceof BigDecimal);
    }

    /**
     * Verify that {@link TestHelper#randomAlphabetic} creates a random {@link String} of alpha characters.
     */
    @Test
    public void randomAlphabetic() {
        assertNotNull(TestHelper.randomAlphabetic(100));
        assertTrue(StringUtils.isAlpha(TestHelper.randomAlphabetic(100)));
        assertTrue(TestHelper.randomAlphabetic(100) instanceof String);
    }

    /**
     * Verify that {@link TestHelper#randomAlphanumeric} creates a random {@link String} of alphanumeric characters.
     */
    @Test
    public void randomAlphanumeric() {
        assertNotNull(TestHelper.randomAlphanumeric(100));
        assertTrue(StringUtils.isAlphanumeric(TestHelper.randomAlphanumeric(100)));
        assertTrue(TestHelper.randomAlphanumeric(100) instanceof String);
    }

    /**
     * Verify that {@link TestHelper#randomNumeric} creates a random {@link String} of numeric characters.
     */
    @Test
    public void randomNumeric() {
        assertNotNull(TestHelper.randomNumeric(100));
        assertTrue(StringUtils.isNumeric(TestHelper.randomNumeric(100)));
        assertTrue(TestHelper.randomNumeric(100) instanceof String);
    }

    /**
     * Verify that {@link TestHelper#randomDate} creates a random {@link Date}.
     */
    @Test
    public void randomDate() {
        assertNotNull(TestHelper.randomDate());
        assertTrue(TestHelper.randomDate() instanceof Date);
    }

    /**
     * Verify that {@link TestHelper#randomBoolean} creates a random {@link Boolean}.
     */
    @Test
    public void randomBoolean() {
        assertNotNull(TestHelper.randomBoolean());
        assertTrue(TestHelper.randomBoolean() instanceof Boolean);
    }

    /**
     * Verify that {@link TestHelper#randomByteArray} creates a random array of bytes.
     */
    @Test
    public void randomByteArray() {
        assertNotNull(TestHelper.randomByteArray());
    }

    /**
     * Verify that {@link TestHelper#randomStringFromList} randomly returns one of the provided list of strings.
     */
    @Test
    public void randomStringFromList() {
        List<String> listOfRandomStrings = new ArrayList<>();
        listOfRandomStrings.add(TestHelper.randomAlphabetic(100));
        listOfRandomStrings.add(TestHelper.randomAlphabetic(100));
        listOfRandomStrings.add(TestHelper.randomAlphanumeric(100));
        listOfRandomStrings.add(TestHelper.randomNumeric(100));
        assertTrue(TestHelper.randomStringFromList(listOfRandomStrings) instanceof String);

        String randomString = TestHelper.randomStringFromList(listOfRandomStrings);
        assertNotNull(randomString);
        assertTrue(listOfRandomStrings.contains(randomString));
    }

    /**
     * Verify that {@link TestHelper#randomRank} creates a random {@link Rank}.
     */
    @Test
    public void randomRank() {
        assertNotNull(TestHelper.randomRank());
        assertTrue(TestHelper.randomRank() instanceof Rank);
    }

    /**
     * Verify that {@link TestHelper#randomSuit} creates a random {@link Suit}.
     */
    @Test
    public void randomSuit() {
        assertNotNull(TestHelper.randomSuit());
        assertTrue(TestHelper.randomSuit() instanceof Suit);
    }

    /**
     * Verify that {@link TestHelper#randomCurrency} creates a random {@link Currency}.
     */
    @Test
    public void randomCurrency() {
        assertNotNull(TestHelper.randomCurrency());
        assertTrue(TestHelper.randomCurrency() instanceof Currency);
    }

    /**
     * Verify that {@link TestHelper#randomHttpMethod} creates a random {@link HttpMethod}.
     */
    @Test
    public void randomHttpMethod() {
        assertNotNull(TestHelper.randomHttpMethod());
        assertTrue(TestHelper.randomHttpMethod() instanceof HttpMethod);
    }

    /**
     * Verify that {@link TestHelper#randomHttpStatus} creates a random {@link HttpStatus}.
     */
    @Test
    public void randomHttpStatus() {
        assertNotNull(TestHelper.randomHttpStatus());
        assertTrue(TestHelper.randomHttpStatus() instanceof HttpStatus);
    }

    /**
     * Verify that {@link TestHelper#randomImageMimeType} creates a random {@link String}.
     */
    @Test
    public void randomImageMimeType() {
        assertNotNull(TestHelper.randomImageMimeType());
        assertTrue(TestHelper.randomImageMimeType() instanceof String);
    }

    /**
     * Verify that {@link TestHelper#randomImageMimeType} creates a random {@link String}.
     */
    @Test
    public void randomMimeType() {
        assertNotNull(TestHelper.randomMimeType());
        assertTrue(TestHelper.randomMimeType() instanceof String);
    }

    /**
     * Verify that when {@link TestHelper#getFieldNamesAndValues} is given an object it returns a map that contains
     * the name of its fields as keys and the corresponding value on the map with the field's value.
     */
    @Test
    public void getFieldNamesAndValues() throws IllegalAccessException {
        // set up random values
        Long id = TestHelper.randomLong();
        String description = TestHelper.randomAlphabetic(100);
        Integer count = TestHelper.randomInteger();
        BigDecimal balance = TestHelper.randomBigDecimal();

        // create test object with random values
        TestObject testObject = new TestObject(id, description, count, balance);

        // exercise the method being tested
        Map fieldNamesAndValues = TestHelper.getFieldNamesAndValues(testObject);

        // assert that the returned map has the correct keys and values derived from the test object
        assertTrue(fieldNamesAndValues.containsKey("id"));
        assertTrue(fieldNamesAndValues.containsKey("description"));
        assertTrue(fieldNamesAndValues.containsKey("count"));
        assertTrue(fieldNamesAndValues.containsKey("balance"));
        assertEquals(id, fieldNamesAndValues.get("id"));
        assertEquals(description, fieldNamesAndValues.get("description"));
        assertEquals(count, fieldNamesAndValues.get("count"));
        assertEquals(balance, fieldNamesAndValues.get("balance"));
    }

    /**
     * The {@link TestHelper#getFieldNamesAndValues} method contains code that handles a {@link IllegalAccessException}
     * that can occur when it is attempting to get values from a {@link java.lang.reflect.Field}. This particular test
     * exercises that condition.
     */
    @Test
    public void getFieldNamesAndValuesIllegalAccessException() throws IllegalAccessException {
        Map fieldNamesAndValues = TestHelper.getFieldNamesAndValues(PrivateConstructorTestObject.class);
        assertFalse(fieldNamesAndValues.containsKey("id"));
        assertFalse(fieldNamesAndValues.containsKey("description"));
        assertFalse(fieldNamesAndValues.containsKey("count"));
        assertFalse(fieldNamesAndValues.containsKey("balance"));
    }

    private class TestObject {
        private Long id;
        private String description;
        private Integer count;
        private BigDecimal balance;
        TestObject(Long id, String description, Integer count, BigDecimal balance) {
            this.id = id;
            this.description = description;
            this.count = count;
            this.balance = balance;
        }
    }

    private class PrivateConstructorTestObject {
        private PrivateConstructorTestObject() {
        }
        private Long id = 1L;
        private String description = "description";
        private Integer count = 10;
        private BigDecimal balance = new BigDecimal(1000);
    }

}
