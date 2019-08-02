package edu.uwm.capstone.util;

public class Mathematics {

    /**
     * This method takes two integers and adds them together
     * @param value1 the first integer to be added
     * @param value2 the second integer to be added
     * @return the sum of the two values
     */
    public int add(int value1, int value2){

        return value1+value2;
    }

    /**
     * This method takes three integers and adds them together
     * @param value1 the first integer to be added
     * @param value2 the second integer to be added
     * @param value3 the third integer to be added
     * @return the sum of the two values
     */
    public int add(int value1, int value2, int value3){
        return value1+value2+value3;
    }

    /**
     * This method takes two doubles and adds them together
     * @param value1 the first double to be added
     * @param value2 the second double to be added
     * @return the sum of the two values
     */
    public double add(double value1, double value2){
        return value1+value2;
    }

    /**
     * This method takes three doubles and adds them together
     * @param value1 the first double to be added
     * @param value2 the second double to be added
     * @param value3 the third double to be added
     * @return the sum of the two values
     */
    public double add(double value1, double value2, double value3){
        return value1+value2+value3;
    }

    /**
     * This method takes two integers and subtracts the second from the first
     * @param value1 the integer to be subtracted from
     * @param value2 the integer to be subtracted
     * @return the result once value2 is subtracted from value1
     */
    public int subtract(int value1, int value2){
        return value1-value2;
    }

    /**
     * This method takes three integers and subtracts them in order
     * @param value1 the integer to be subtracted from
     * @param value2 the first integer to be subtracted
     * @param value3 the second integer to be subtracted
     * @return the result once value2 is subtracted from value1 and value3 is subtracted from that result
     */
    public int subtract(int value1, int value2, int value3){
        return value1-value2-value3;
    }

    /**
     * This method takes two doubles and subtracts the second from the first
     * @param value1 the double to be subtracted from
     * @param value2 the double to be subtracted
     * @return the result once value2 is subtracted from value1
     */
    public double subtract(double value1, double value2){
        return value1-value2;
    }

    /**
     * This method takes three doubles and subtracts them in order
     * @param value1 the double to be subtracted from
     * @param value2 the first double to be subtracted
     * @param value3 the second double to be subtracted
     * @return the result once value2 is subtracted from value1 and value3 is subtracted from that result
     */
    public double subtract(double value1, double value2, double value3){
        return value1-value2-value3;
    }

    /**
     * This method takes two integers and multiplies them together
     * @param value1 the first integer to be mulitplied
     * @param value2 the second integer to be multiplied
     * @return the product of the two values
     */
    public int multiply(int value1, int value2){
        return value1*value2;
    }

    /**
     * This method takes three integers and multiplies them together
     * @param value1 the first integer to be mulitplied
     * @param value2 the second integer to be multiplied
     * @param value3 the third integer to be multiplied
     * @return the product of the three values
     */
    public int multiply(int value1, int value2, int value3){
        return value1*value2*value3;
    }

    /**
     * This method takes two doubles and multiplies them together
     * @param value1 the first double to be mulitplied
     * @param value2 the second double to be multiplied
     * @return the product of the two values
     */
    public double multiply(double value1, double value2){
        return value1*value2;
    }

    /**
     * This method takes three doubles and multiplies them together
     * @param value1 the first double to be mulitplied
     * @param value2 the second double to be multiplied
     * @param value3 the third double to be multiplied
     * @return the product of the three values
     */
    public double multiply(double value1, double value2, double value3){
        return value1*value2*value3;
    }

    /**
     * This method takes two integers and divides the first by the second
     * @param value1 the dividend
     * @param value2 the divisor
     * @return the quotient that results
     */
    public int divide(int value1, int value2){
        assert (value2 != 0): "You cannot divide by 0";
        return value1/value2;
    }

    /**
     * This method takes three integers and divides the first by the second and then divides that quotient by the third
     * @param value1 the dividend
     * @param value2 the divisor
     * @param value3 the second divisor
     * @return the quotient that results at the end
     */
    public int divide(int value1, int value2, int value3){
        assert (value2 != 0 && value3 != 0): "You cannot divide by 0";
        return value1/value2/value3;
    }

    /**
     * This method takes two double and divides the first by the second
     * @param value1 the dividend
     * @param value2 the divisor
     * @return the quotient that results
     */
    public double divide(double value1, double value2){
        assert (value2 != 0): "You cannot divide by 0";
        return value1/value2;
    }

    /**
     * This method takes three double and divides the first by the second and that quotient by the third
     * @param value1 the dividend
     * @param value2 the divisor
     * @param value3 the second divisor
     * @return the quotient that results
     */
    public double divide(double value1, double value2, double value3){
        assert (value2 != 0 && value3 != 0): "You cannot divide by 0";
        return value1/value2/value3;
    }

    /**
     * This method finds the square root of a value
     * @param value the number that will be square rooted
     * @return the square root of the value
     */
    public double squareRoot(double value){
        assert (value >= 0): "You cannot find the square root of a negative number";
        return Math.sqrt(value);
    }
}

