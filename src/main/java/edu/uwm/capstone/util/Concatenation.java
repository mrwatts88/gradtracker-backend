package edu.uwm.capstone.util;

import org.springframework.util.Assert;

/**
 * Concatenation is a series of interconnected things or events.
 */
public class Concatenation {

    /**
     * This method takes two separate values and combines them together.
     * @param firstString {@link String} value of the first thing to be concatenated
     * @param secondString {@link String} value of the second thing to be concatenated
     * @return {@link String} value of the provided strings concatenated together
     */
    public String concatenate(String firstString, String secondString) {
        Assert.notNull(firstString, "firstString cannot be null");
        Assert.notNull(secondString, "secondString cannot be null");
        return firstString+secondString;
    }

    /**
     * This method takes three separate values and combines them together.
     * @param firstString {@link String} value of the first thing to be concatenated
     * @param secondString {@link String} value of the second thing to be concatenated
     * @param thirdString {@link String} value of the third thing to be concatenated
     * @return
     */
    public String concatenate(String firstString, String secondString, String thirdString) {
        Assert.notNull(firstString, "firstString cannot be null");
        Assert.notNull(secondString, "secondString cannot be null");
        Assert.notNull(thirdString, "thirdString cannot be null");
        return firstString+secondString+thirdString;
    }

}
