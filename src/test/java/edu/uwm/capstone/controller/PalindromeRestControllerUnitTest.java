package edu.uwm.capstone.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PalindromeRestControllerUnitTest {

    private PalindromeRestController palindromeRestController;

    @Before
    public void setUp() {
        palindromeRestController = new PalindromeRestController();
    }

    /**
     * Verify that {@link PalindromeRestController#isPalindrome} correctly identifies known palindromes.
     */
    @Test
    public void verifyKnownPalindromes() {
        String[] knownPalindromes = {"redivider", "noon", "civic", "radar", "level", "rotor", "kayak", "reviver",
                "racecar", "redder", "madam", "refer"};
        for (String value : knownPalindromes) {
            assertTrue(value + " is a known palindrome", palindromeRestController.isPalindrome(value));
        }
    }

    /**
     * Verify that {@link PalindromeRestController#isPalindrome} correctly identifies known palindromes.
     */
    @Test
    public void verifyInvalidPalindromes() {
        String[] knownPalindromes = {"rdivder", "nzoont", "cuvic", "roder", "livul", "rater", "koyuk", "ravevir",
                "rececar", "rudder", "modim", "rifurs"};
        for (String value : knownPalindromes) {
            assertFalse(value + " is an invalid palindrome", palindromeRestController.isPalindrome(value));
        }
    }

    /**
     * Verify that providing a null value to {@link PalindromeRestController#isPalindrome} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyNullIllegalArgumentException() {
        palindromeRestController.isPalindrome(null);
    }

}
