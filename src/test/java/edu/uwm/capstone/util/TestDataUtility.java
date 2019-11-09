package edu.uwm.capstone.util;

import edu.uwm.capstone.model.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TestDataUtility {

    /**
     * This is a utility class and as such it only has a private constructor.
     */
    protected TestDataUtility() {
    }

    /**
     * Generate a {@link User} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link User}
     */
    public static User userWithTestValues() {
        return User.builder()
                .firstName(randomAlphabetic(randomInt(10, 25)))
                .lastName(randomAlphabetic(randomInt(10, 25)))
                .password(randomAlphabetic(randomInt(10, 50)))
                .pantherId(randomAlphabetic(randomInt(9, 10)))
                .email(randomAlphabetic(randomInt(5, 15)) + "@" + randomAlphabetic(randomInt(3, 10)))
                .roleNames(new HashSet<>())
                .authorities(new HashSet<>())
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build();
    }

    /**
     * Generate a {@link FormDefinition} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link FormDefinition}
     */
    public static FormDefinition formDefWithTestValues() {
        FormDefinition formDefinition = new FormDefinition();
        formDefinition.setName(randomAlphabetic(randomInt(1, 100)));
        ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<>();
        int j = randomInt(5, 20);
        for (int i = 0; i < j; i++) {
            fieldDefinitions.add(fieldDefWithTestValues());
        }
        formDefinition.setFieldDefs(fieldDefinitions);
        return formDefinition;
    }

    /**
     * Generate a {@link FieldDefinition} object that is fully loaded with random values for testing purposes.
     *
     * @return {@link FieldDefinition}
     */
    public static FieldDefinition fieldDefWithTestValues() {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setLabel(randomAlphabetic(randomInt(1, 50)));
        fieldDefinition.setInputType(randomAlphabetic(randomInt(1, 10))); // this may need to change
        fieldDefinition.setDataType(randomAlphabetic(randomInt(1, 10)));  // this may need to change
        fieldDefinition.setFieldIndex(randomInt());
        return fieldDefinition;
    }

    /**
     * Generate a {@link Form} object that is fully loaded with random values for testing purposes,
     * given a persisted {@link FormDefinition}.
     *
     * @return {@link Form}
     */
    public static Form formWithTestValues(FormDefinition formDefinition, Long userId) {
        Form form = new Form();
        form.setApproved(randomBoolean());
        form.setUserId(userId);
        form.setFormDefId(formDefinition.getId());
        ArrayList<Field> fields = new ArrayList<>();
        for (FieldDefinition fd : formDefinition.getFieldDefs()) {
            fields.add(fieldWithTestValues(fd));
        }
        form.setFields(fields);
        return form;
    }

    /**
     * Generate a {@link Field} object that is fully loaded with random values for testing purposes,
     * given a persisted {@link FieldDefinition}.
     *
     * @return {@link Field}
     */
    public static Field fieldWithTestValues(FieldDefinition fieldDefinition) {
        Field field = new Field();
        field.setData(randomAlphabetic(randomInt(10, 200)));
        field.setFieldDefId(fieldDefinition.getId());
        return field;
    }

    /**
     * Generate a random {@link Boolean}.
     *
     * @return random {@link Boolean}
     */
    public static Boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    /**
     * Generate a random {@link Long} using a minimum value of 1L and a maximum value of {@link Long#MAX_VALUE}.
     *
     * @return random {@link Long}
     */
    public static Long randomLong() {
        return randomLong(1L, Long.MAX_VALUE);
    }

    /**
     * Generate a random {@link Long} using the provided minimum and maximum values.
     *
     * @param min {@link Long} minimum value
     * @param max {@link Long} maximum value
     * @return random {@link Long}
     */
    public static Long randomLong(Long min, Long max) {
        return new Random().longs(min, max).findAny().getAsLong();
    }

    /**
     * Generate a random {@link Integer} using a minimum value of 1 and a maximum value of {@link Integer#MAX_VALUE}.
     *
     * @return random {@link Integer}
     */
    public static int randomInt() {
        return randomInt(1, Integer.MAX_VALUE);
    }

    /**
     * Generate a random {@link int} using the provided minimum and maximum values.
     *
     * @param min {@link int} minimum value
     * @param max {@link int} maximum value
     * @return random {@link int}
     */
    public static int randomInt(int min, int max) {
        return new Random().ints(min, max).findAny().getAsInt();
    }

    /**
     * Generate a {@link String} that contains the provided number of random alphabetic characters.
     *
     * @param characterCount Number of characters
     * @return random {@link String} of alphabetic characters
     */
    public static String randomAlphabetic(int characterCount) {
        return RandomStringUtils.randomAlphabetic(characterCount);
    }

    /**
     * Generate a {@link String} that contains the provided number of random alphanumeric characters.
     *
     * @param characterCount Number of characters
     * @return random {@link String} of alphanumeric characters
     */
    public static String randomAlphanumeric(int characterCount) {
        return RandomStringUtils.randomAlphanumeric(characterCount);
    }

    public static LocalDateTime randomLocalDateTime() {
        LocalDateTime start = LocalDateTime.of(1900, randomMonth(), 1, randomInt(0, 23), randomInt(1, 59));
        long days = ChronoUnit.DAYS.between(start, LocalDateTime.now());
        return start.plusDays(new Random().nextInt((int) days + 1));
    }

    public static Month randomMonth() {
        List<Month> months = Arrays.asList(Month.values());
        int index = new Random().nextInt(months.size());
        return months.get(index);
    }

}
