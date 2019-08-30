package edu.uwm.capstone.controller;

import edu.uwm.capstone.util.Palindrome;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class PalindromeRestController {

    public static final String PALINDROME_PATH = "/palindrome/{value}";

    /**
     * This endpoint determines whether or not the provided value is a palindrome.
     *
     * @param value {@link String}
     * @return boolean representing whether or not the provided value is a palindrome
     */
    @ApiOperation(value = "Determine whether or not the provided value is a palindrome")
    @GetMapping(value = PALINDROME_PATH)
    public boolean isPalindrome(@PathVariable String value) {
        return new Palindrome().isPalindrome(value);
    }

}
