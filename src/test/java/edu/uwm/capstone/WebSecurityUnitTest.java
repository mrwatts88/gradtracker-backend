package edu.uwm.capstone;

import edu.uwm.capstone.security.WebSecurity;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class WebSecurityUnitTest {
    private static WebSecurity webSecurity;

    @BeforeClass
    public static void setUp() {
        webSecurity = new WebSecurity();
    }

    @Test
    public void passwordEncoder() {
        assertNotNull(webSecurity.passwordEncoder());
    }

    @Test
    public void userDetailsService() {
        assertNotNull(webSecurity.userDetailsService());
    }
}
