package edu.uwm.capstone.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * A palindrome is a word, phrase, or sequence that reads the same backward as forward.
 */
public class Palindrome {

    /**
     * Determine whether or not the provided value is a palindrome.
     * @param value {@link String}
     * @return boolean representing whether or not the provided value is a palindrome
     */
    public boolean isPalindrome(String value) {
        Assert.notNull(value, "The value cannot be null");
        return value.equals(StringUtils.reverse(value));
    }

}
