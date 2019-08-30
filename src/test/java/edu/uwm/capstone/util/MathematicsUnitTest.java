package edu.uwm.capstone.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathematicsUnitTest {

    private Mathematics mathematics;

    @Before
    public void setUp() {
        mathematics = new Mathematics();
    }

    /**
     * Verify that {@link Mathematics#add(int, int)} correctly adds 10 and 42 into 52.
     */
    @Test
    public void add() {
        int result = mathematics.add(10, 42);
        assertEquals(52, result);
    }

    /**
     * Verify that {@link Mathematics#add(int, int, int)} correctly adds 1, 2, and 3 to 6.
     */
    @Test
    public void addThree() {
        int result = mathematics.add(1, 2, 3);
        assertEquals(6, result);
    }

    /**
     * Verify that {@link Mathematics#add(double, double)} correctly adds 1.1 and 2.2 into 3.3.
     */
    @Test
    public void addDouble() {
        double result = mathematics.add(1.1, 2.2);
        assertEquals(3.3, result, 0.01);
    }

    /**
     * Verify that {@link Mathematics#add(double, double, double)} correctly adds 1.1, 2.2, and 3.3 to 6.6.
     */
    @Test
    public void addDoubleThree() {
        double result = mathematics.add(1.1, 2.2, 3.3);
        assertEquals(6.6, result, 0.01);
    }

    /**
     * Verify that {@link Mathematics#subtract(int, int)} correctly subtracts 30 from 105 to have a result of 75.
     */
    @Test
    public void subtract() {
        int result = mathematics.subtract(105, 30);
        assertEquals(75, result);
    }

    /**
     * Verify that {@link Mathematics#subtract(int, int, int)} correctly subtracts 10-5-2 to equal 3.
     */
    @Test
    public void subtractThree() {
        int result = mathematics.subtract(10, 5, 2);
        assertEquals(3, result);
    }

    /**
     * Verify that {@link Mathematics#subtract(double, double)} correctly subtracts 3.3-2.2 to equal 1.1.
     */
    @Test
    public void subtractDouble() {
        double result = mathematics.subtract(3.3, 2.2);
        assertEquals(1.1, result, .01);
    }

    /**
     * Verify that {@link Mathematics#subtract(double, double, double)} correctly subtracts 9.9-5.5-3.3 to equal 1.1.
     */
    @Test
    public void subtractDoubleThree() {
        double result = mathematics.subtract(9.9, 5.5, 3.3);
        assertEquals(1.1, result, .01);
    }

    /**
     * Verify that {@link Mathematics#multiply(int, int)} correctly multiplies 6 and 5 into 30.
     */
    @Test
    public void multiply() {
        int result = mathematics.multiply(6, 5);
        assertEquals(30, result);
    }

    /**
     * Verify that {@link Mathematics#multiply(int, int, int)} correctly multiplies 4*5*2 to equal 40.
     */
    @Test
    public void multiplyThree() {
        int result = mathematics.multiply(4, 5, 2);
        assertEquals(40, result);
    }

    /**
     * Verify that {@link Mathematics#multiply(double, double)} correctly multiplies 10.2 and 3.0 to equal 30.6.
     */
    @Test
    public void multiplyDouble() {
        double result = mathematics.multiply(10.2, 3.0);
        assertEquals(30.6, result, .01);
    }

    /**
     * Verify that {@link Mathematics#multiply(double, double, double)} correctly multiplies 10.2*3.0*2.0 to equal 61.2.
     */
    @Test
    public void multiplyDoubleThree() {
        double result = mathematics.multiply(10.2, 3.0, 2.0);
        assertEquals(61.2, result, .01);
    }

    /**
     * Verify that {@link Mathematics#divide(int, int)} correctly divides 100 by 4 to result in 25.
     */
    @Test
    public void divide() {
        int result = mathematics.divide(100, 4);
        assertEquals(25, result);
    }

    /**
     * Verify that {@link Mathematics#divide(int, int, int)} correctly divides 100 by 4 by 5 to result in 5.
     */
    @Test
    public void divide3() {
        int result = mathematics.divide(100, 4, 5);
        assertEquals(5, result);
    }

    /**
     * Verify that {@link Mathematics#divide(double, double)} correctly divides 100.4 by 4.0 to result in 25.1.
     */
    @Test
    public void divideDouble() {
        double result = mathematics.divide(100.4, 4.0);
        assertEquals(25.1, result, .01);
    }

    /**
     * Verify that {@link Mathematics#divide(double, double)} correctly divides 100.4 by 4.0 to result in 25.1.
     */
    @Test
    public void divideDoubleThree() {
        double result = mathematics.divide(100.4, 4.0, 5.0);
        assertEquals(5.02, result, .01);
    }

    /**
     * Verify that {@link Mathematics#squareRoot(double)} correctly square roots .
     */
    @Test
    public void squareRoot() {
        double result = mathematics.squareRoot(9);
        assertEquals(3, result, .0001);
        result = mathematics.squareRoot(16);
        assertEquals(4, result, .0001);
        result = mathematics.squareRoot(25);
        assertEquals(5, result, .0001);
        result = mathematics.squareRoot(64);
        assertEquals(8, result, .0001);
    }

    /**
     * Verify that providing a 0 as the second value to {@link Mathematics#divide(int, int)} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyDivideAssertion() {
        mathematics.divide(5, 0);
    }

    /**
     * Verify that providing a 0 as the second value to {@link Mathematics#divide(double, double)} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyDivideDoubleAssertion() {
        mathematics.divide(5.0, 0.0);
    }

    /**
     * Verify that providing a 0 as the second or third value to {@link Mathematics#divide(int, int, int)} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyDivideThreeAssertion() {
        mathematics.divide(5, 8, 0);
    }

    /**
     * Verify that providing a 0 as the second or third value to {@link Mathematics#divide(double, double, double)} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyDivideDoubleThreeAssertion() {
        mathematics.divide(5.0, 0.0, 8.2);
    }

    /**
     * Verify that providing a negative number as value to {@link Mathematics#squareRoot(double)} produces an {@link IllegalArgumentException}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void verifyNegativeSquareRoot() {
        mathematics.squareRoot(-102);
    }
}
