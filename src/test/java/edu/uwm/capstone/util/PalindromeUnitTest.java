package edu.uwm.capstone.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PalindromeUnitTest {

    private Palindrome palindrome;

    @Before
    public void setUp() {
        palindrome = new Palindrome();
    }

    /**
     * Verify that {@link Palindrome#isPalindrome} correctly identifies known palindromes.
     */
    @Test
    public void verifyKnownPalindromes() {
        String[] knownPalindromes = {"redivider", "noon", "civic", "radar", "level", "rotor", "kayak", "reviver",
                "racecar", "redder", "madam", "refer"};
        for (String value : knownPalindromes) {
            assertTrue(value + " is a known palindrome", palindrome.isPalindrome(value));
        }
    }

    /**
     * Verify that {@link Palindrome#isPalindrome} correctly identifies non-palindromes.
     */
    @Test
    public void verifyInvalidPalindromes() {
        String[] knownPalindromes = {"rdivder", "nzoont", "cuvic", "roder", "livul", "rater", "koyuk", "ravevir",
                "rececar", "rudder", "modim", "rifurs"};
        for (String value : knownPalindromes) {
            assertFalse(value + " is an invalid palindrome", palindrome.isPalindrome(value));
        }
    }

    /**
     * Verify that providing a null value to {@link Palindrome#isPalindrome} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyNullIllegalArgumentException() {
        palindrome.isPalindrome(null);
    }

}
