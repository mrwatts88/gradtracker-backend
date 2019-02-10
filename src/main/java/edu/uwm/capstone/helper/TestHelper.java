package edu.uwm.capstone.helper;

import com.google.common.net.MediaType;
import edu.uwm.capstone.model.cards.Rank;
import edu.uwm.capstone.model.cards.Suit;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

public class TestHelper {

    private static final Random random = new Random();

    private TestHelper() { }

    /**
     * Create a random {@link Long}.
     *
     * @return {@link Long} between 1L and {@link Long#MAX_VALUE}
     */
    public static Long randomLong() {
        return new Random().longs(1L, Long.MAX_VALUE).findAny().getAsLong();
    }

    /**
     * Create a random {@link Double}.
     *
     * @return {@link Double} between 1 and {@link Double#MAX_VALUE}
     */
    public static Double randomDouble() {
        return new Random().doubles(1, Double.MAX_VALUE).findAny().getAsDouble();
    }

    /**
     * Create a random {@link Integer}.
     *
     * @return {@link Integer} between 1 and {@link Integer#MAX_VALUE}
     */
    public static Integer randomInteger() {
        return randomInteger(1, Integer.MAX_VALUE);
    }

    /**
     * Create a random {@link Integer} between the provided start and stop values.
     *
     * @param start Lower boundary of the random number to be generated
     * @param stop  Upper boundary of the random number to be generated
     * @return {@link Integer} between the provided start and stop values
     */
    public static Integer randomInteger(int start, int stop) {
        return new Random().ints(start, stop).findAny().getAsInt();
    }

    /**
     * Create a random {@link BigDecimal}.
     *
     * @return {@link BigDecimal} between {@link Long#MIN_VALUE} and {@link Long#MAX_VALUE}
     */
    public static BigDecimal randomBigDecimal() {
        BigDecimal min = new BigDecimal(Long.MIN_VALUE);
        BigDecimal max = new BigDecimal(Long.MAX_VALUE);
        return min.add(BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)));
    }

    /**
     * Create a string containing the specified number of randomized alpha characters.
     *
     * @param characterCount Number of alpha characters to be contained in the randomized string
     * @return {@link String} containing the specified number of randomized alpha characters
     */
    public static String randomAlphabetic(int characterCount) {
        return RandomStringUtils.randomAlphabetic(characterCount);
    }

    /**
     * Create a string containing the specified number of randomized alphanumeric characters.
     *
     * @param characterCount Number of alphanumeric characters to be contained in the randomized string
     * @return {@link String} containing the specified number of randomized alphanumeric characters
     */
    public static String randomAlphanumeric(int characterCount) {
        return RandomStringUtils.randomAlphanumeric(characterCount);
    }

    /**
     * Create a string containing the specified number of randomized numeric characters.
     *
     * @param characterCount Number of numeric characters to be contained in the randomized string
     * @return {@link String} containing the specified number of randomized numeric characters
     */
    public static String randomNumeric(int characterCount) {
        return RandomStringUtils.randomNumeric(characterCount);
    }

    /**
     * Create a random {@link Date}.
     *
     * @return {@link Date} based on a value generated from {@link #randomLong}
     */
    public static Date randomDate() {
        return new Date(randomLong());
    }

    /**
     * Random true or false value.
     *
     * @return {@link Boolean} that was randomly selected
     */
    public static Boolean randomBoolean() {
        return (randomInteger(0, 100) % 2) == 1;
    }

    /**
     * Create a byte array of random byte values.
     *
     * @return byte[] containing the randomized byte values
     */
    public static byte[] randomByteArray() {
        int max = 100;
        int min = 20;
        int randomByteCount = random.nextInt(max - min + 1) + min;
        byte[] randomBytes = new byte[randomByteCount];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * Given a list of strings randomly select and return one.
     *
     * @param strings List of Strings
     * @return {@link String} that was randomly selected from the provided list
     */
    public static String randomStringFromList(List<String> strings) {
        String value = null;
        if (strings != null && !strings.isEmpty()) {
            int index = new Random().nextInt(strings.size());
            value = strings.get(index);
        }
        return value;
    }

    /**
     * Randomly select a {@link Rank} from the supported methods within {@link Rank#values}.
     *
     * @return {@link Rank} that was randomly selected
     */
    public static Rank randomRank() {
        Rank rank = null;
        List<Rank> ranks = Arrays.asList(Rank.values());
        if (!ranks.isEmpty()) {
            int index = new Random().nextInt(ranks.size());
            rank = ranks.get(index);
        }
        return rank;
    }

    /**
     * Randomly select a {@link Suit} from the supported methods within {@link Suit#values}.
     *
     * @return {@link Suit} that was randomly selected
     */
    public static Suit randomSuit() {
        Suit suit = null;
        List<Suit> suits = Arrays.asList(Suit.values());
        if (!suits.isEmpty()) {
            int index = new Random().nextInt(suits.size());
            suit = suits.get(index);
        }
        return suit;
    }

    /**
     * Randomly select a {@link Currency} from the supported currencies within {@link Currency#getAvailableCurrencies}.
     *
     * @return {@link Currency} that was randomly selected
     */
    public static Currency randomCurrency() {
        Currency currency = null;
        List<Currency> currencies = new ArrayList<>();
        currencies.addAll(Currency.getAvailableCurrencies());
        if (!currencies.isEmpty()) {
            int index = new Random().nextInt(currencies.size());
            currency = currencies.get(index);
        }
        return currency;
    }

    /**
     * Randomly select a {@link HttpMethod} from the supported methods within {@link HttpMethod#values}.
     *
     * @return {@link HttpMethod} that was randomly selected
     */
    public static HttpMethod randomHttpMethod() {
        HttpMethod httpMethod = null;
        List<HttpMethod> httpMethods = Arrays.asList(HttpMethod.values());
        if (!httpMethods.isEmpty()) {
            int index = new Random().nextInt(httpMethods.size());
            httpMethod = httpMethods.get(index);
        }
        return httpMethod;
    }

    /**
     * Randomly select a {@link HttpStatus} from the supported statuses within {@link HttpStatus#values}.
     *
     * @return {@link HttpStatus} that was randomly selected
     */
    public static HttpStatus randomHttpStatus() {
        HttpStatus httpStatus = null;
        List<HttpStatus> httpStatuses = Arrays.asList(HttpStatus.values());
        if (!httpStatuses.isEmpty()) {
            int index = new Random().nextInt(httpStatuses.size());
            httpStatus = httpStatuses.get(index);
        }
        return httpStatus;
    }

    /**
     * Randomly select the string value an image {@link MediaType} from the provided list of {@link MediaType}s.
     *
     * @return {@link String} value of the {@link MediaType} that was randomly selected
     */
    public static String randomImageMimeType() {
        List<String> mimeTypes = new ArrayList<>();
        mimeTypes.add(MediaType.GIF.toString());
        mimeTypes.add(MediaType.PNG.toString());
        mimeTypes.add(MediaType.JPEG.toString());
        return randomStringFromList(mimeTypes);
    }

    /**
     * Randomly select the string value a {@link MediaType} from the provided list of {@link MediaType}s.
     *
     * @return {@link String} value of the {@link MediaType} that was randomly selected
     */
    public static String randomMimeType() {
        List<String> mimeTypes = new ArrayList<>();
        mimeTypes.add(MediaType.HTML_UTF_8.toString());
        mimeTypes.add(MediaType.JSON_UTF_8.toString());
        mimeTypes.add(MediaType.XML_UTF_8.toString());
        mimeTypes.add(MediaType.GIF.toString());
        mimeTypes.add(MediaType.PNG.toString());
        mimeTypes.add(MediaType.JPEG.toString());
        return randomStringFromList(mimeTypes);
    }

    /**
     * Given any object return a map that contains the name of its fields as keys
     * and the corresponding value on the map will be the field's value.
     *
     * @param object {@link Object} supports any java object
     * @return Map containing the object's fields as keys and those field's values as the corresponding map values
     */
    public static Map<String, Object> getFieldNamesAndValues(Object object) {
        Class clazz = object.getClass();
        Map<String, Object> fieldMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isSynthetic()) {
                field.setAccessible(true);
                Object fieldValue = null;
                try {
                    fieldValue = field.get(object);
                    fieldMap.put(field.getName(), fieldValue);
                } catch (IllegalAccessException e) {
                    // swallow the error and try to continue processing the provided image
                }
            }
        }
        return fieldMap;
    }

}
