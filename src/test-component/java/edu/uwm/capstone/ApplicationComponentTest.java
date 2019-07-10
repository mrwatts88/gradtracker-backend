package edu.uwm.capstone;

import org.junit.Test;

/**
 * This test class exercises the spring boot based {@link Application}.
 */
@SuppressWarnings("squid:S2699") //suppress the warning about the lack of assert statements
public class ApplicationComponentTest {

    @Test
    public void main() {
        String[] args = {};
        Application.main(args);
    }
}
