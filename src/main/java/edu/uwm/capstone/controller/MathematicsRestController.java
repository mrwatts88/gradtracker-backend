package edu.uwm.capstone.controller;

import edu.uwm.capstone.util.Mathematics;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathematicsRestController {

    public static final String MATHEMATICS_ADD_PATH = "/mathematics/add/{value}/{value2}";
    public static final String MATHEMATICS_SUBTRACT_PATH = "/mathematics/subtract/{value}/{value2}";
    public static final String MATHEMATICS_MULTIPLY_PATH = "/mathematics/multiply/{value}/{value2}";
    public static final String MATHEMATICS_DIVIDE_PATH = "/mathematics/divide/{value}/{value2}";
    public static final String MATHEMATICS_SQUAREROOT_PATH = "/mathematics/squareRoot/{value}/{value2}";

    /**
     * This endpoint determines the result of adding the two provided values.
     * @param value {@link int} the first integer being added
     * @param value2 {@link int} the second integer being added
     * @return integer representing the sum of the two imputed values
     */
    @ApiOperation(value = "Add the values")
    @GetMapping(value = MATHEMATICS_ADD_PATH)
    public int add(@PathVariable int value, @PathVariable int value2) {
        return new Mathematics().add(value, value2);
    }

    /**
     * This endpoint determines the result of subtracting the second provided value from the first
     * @param value the integer to be subtracted from
     * @param value2 the integer to be subtracted
     * @return the result after the subtraction
     */
    @ApiOperation(value = "Subtract the second number from the first")
    @GetMapping(value = MATHEMATICS_SUBTRACT_PATH)
    public int subtract(@PathVariable int value, @PathVariable int value2) {
        return new Mathematics().subtract(value, value2);
    }

    /**
     * This endpoint determines the result of multiplying the two provided values.
     * @param value {@link int} the first integer being multiplied
     * @param value2 {@link int} the second integer being multiplied
     * @return integer representing the product of the two imputed values
     */
    @ApiOperation(value = "Multiply the values")
    @GetMapping(value = MATHEMATICS_MULTIPLY_PATH)
    public int multiply(@PathVariable int value, @PathVariable int value2) {
        return new Mathematics().multiply(value, value2);
    }

    /**
     * This endpoint determines the result of dividing the first value by the second.
     * @param value {@link int} the dividend
     * @param value2 {@link int} the divisor
     * @return integer representing the final quotient
     */
    @ApiOperation(value = "Divide the dividend by the divisor")
    @GetMapping(value = MATHEMATICS_DIVIDE_PATH)
    public int divide(@PathVariable int value, @PathVariable int value2) {
        return new Mathematics().divide(value, value2);
    }

    /**
     * This endpoint determines the result of square root of given number.
     * @param value {@link double} the number
     * @return double representing the square root of number provided
     */
    @ApiOperation(value = "Sqaure Root of number")
    @GetMapping(value = MATHEMATICS_SQUAREROOT_PATH)
    public double divide(@PathVariable double value) {
        return new Mathematics().squareRoot(value);
    }
}
