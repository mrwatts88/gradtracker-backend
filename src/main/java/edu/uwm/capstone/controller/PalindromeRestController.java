package edu.uwm.capstone.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uwm.capstone.util.Palindrome;
import io.swagger.annotations.ApiOperation;

@RestController
public class PalindromeRestController {

    public static final String PALINDROME_PATH = "/palindrome/{value}";

    /**
     * This endpoint determines whether or not the provided value is a palindrome.
     * @param value {@link String}
     * @return boolean representing whether or not the provided value is a palindrome
     */
    @ApiOperation(value = "Determine whether or not the provided value is a palindrome")
    @RequestMapping(value = PALINDROME_PATH, method = RequestMethod.GET)
    public boolean isPalindrome(@PathVariable String value) {
        return new Palindrome().isPalindrome(value);
    }

}
