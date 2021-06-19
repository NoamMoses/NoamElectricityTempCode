package com.noam.noamelectricity.functions;

import android.widget.EditText;

import java.util.Locale;

public class helperFunctions {

    public static boolean inputIsEmpty(EditText input) {
        /*
        * Explanation:
        *   Checking whether an EditText input is empty, and returning 0/1
        * Return:
        *   true - empty
        *   false - not empty
        * */

        return input.getText().toString().matches("");
    }

    static boolean checkZero(double num, int size) {
        /*
         * Explanation:
         *   The function gets a double-type number, and its "size" (how big, "0-wise")
         *   And checks if its decimal digit is zero or not
         * Return:
         *   0: The number's decimal point IS 0. eg 1.0
         *   1: the number's decimal point IS NOT 0. e.g 1.5
         * */

        return returnSplitedNumber(num, size, 1).equalsIgnoreCase("0");
    }

    static boolean checkZeroSimple(double num) {
        /*
         * Explanation:
         *   Gets a double-type number and returns true if its decimal point is 0
         * Return: same as checkZero
         * */
        return String.valueOf(num).split("[.]")[1].equalsIgnoreCase("0");
    }

    static String returnSplitedNumber(double num, int size, int index) {
        /*
         * Gets a double type number and divide it and returns the requested index after spliting to get the decimal point
         * num: input number
         * size: division number
         * index: index to return
         * For example:
         * 	parameters: 1000, 1000, 0
         * 	Returns: 1000.0/1000=1.0 - 1
         * 		If the last parameter (index) is 1, then it will return the second half - the decimal point.
         * 		Useful to check if the division is .0 or .5 for example (like in the function checkZero)
         * */
        return String.valueOf(num/size).split("[.]")[index];
    }

    static String removeDecimal(double num) {
        /*
         * Explanation:
         *   Gets a double-type number and removes its decimal point
         * - or more accurately, removes its decimal point
         * - but in this case, of my code, it will always be a zero, because I won't remove any other decimal point
         *
         * Notes:
         *   Previously called: removeZero
         * */
        // return String.valueOf(num).split("[.]")[0];
        return String.valueOf(num).split("[.]")[0];
    }

    public static String analyseDot(double num, boolean k, boolean str) {
        /*
         * Explanation:
         *   Getting a double-type number, and returning it as a formatted string
         * Parameters:
         *   num:
         * 	   The double-type number
         *   k:
         * 	   True:
         *       Returns 1 instead of 1000
         *     False:
         *       Keeps the number the same
         *   str:
         * 	   True:
         *       Adds a matching letter to the output:
         *         k for thousands
         *         m for millions
         *         g for billions (9 zeros)
         *     False:
         *       Keeps the number the same
         * Examples:
         * 	Input parameters: 1000.0, true, true
         * 	Output: 1k
         * Notes:
         * 	- The function always gets rid of the decimal point if it's a zero
         *  - Assuming the numbers are always positive
         *  - The range values are not with the *high* limit:
         *    For example 1,000-1,000,000 includes 1,000 but not 1,000,000
         */
        String result;

        if (num < 1000) { // 0-999 - just removing the .0, since there is no k or m to add
            if (checkZeroSimple(num)) {
                return removeDecimal(num);
            } else { // For example 1.2
                System.out.println("CAN YOU SEE IT?");
                return String.format(Locale.getDefault(), "%.2f", num);
            }
        } else if (num >= 1000 && num < 1000000) { // K RANGE | 1,000-1,000,000 (not included)
            if (checkZero(num, 1000)) { // Decimal IS .0
                System.out.println("- k, there is .0, " + num/1000);
                if (k) { // Returns 1 instead of 1000
                    /*
                     * TODO check if the /1000 should be num/1000 or String.valueOf(num).split("[.]")[0])/1000000)
                     * where the division is in the end - it works the same with 1,000, but not the same with 1,000,000, but need to test with more numbers
                     * - if it's in the end, it won't work with big numbers, like 1,000,000 - because it has e, and then it messes up everything
                     * but it works the same with smaller numbers, like 1,000 for example
                     */
                    result = returnSplitedNumber(num, 1000, 0); // Change from 1000 to 1
                    if (str) { // Add k (thousands) - 1k instead of 1
                        result += "K";
                    }
                    return result;
                } else { // Return the number without a decimal point, and without a letter
                    return removeDecimal(num);
                }
            } else { // Decimal IS NOT .0
                result = String.valueOf(num/1000);
                if (str) { // Add k to the return value
                    result += "K";
                }
                return result; // Return the value with or without k, but with the decimal point, since it's not .0
            }
        } else if (num >= 1000000 && num < 1000000000) { // M RANGE | 1,000,000-1,000,000,000
            if (checkZero(num, 1000000)) { // Decimal IS .0
                if (k) {
                    result = String.valueOf(Integer.parseInt(String.valueOf(num/1000000).split("[.]")[0]));  // Change from 1000000 to 1
                    if (str) {
                        result+="M";
                    }
                    return result;
                } else {
                    return removeDecimal(num);
                }
            } else { // Decimal IS NOT .0
                result = String.valueOf(num/1000000);
                if (str) {
                    result+="M";
                }
                return result;
            }
        } else if (num >= 1000000000) { // G RANGE | Above 1,000,000,000 (one billion)
            if (checkZero(num, 1000000000)) { // Decimal IS .0
                if (k) {
                    result = String.valueOf(Integer.parseInt(String.valueOf(num/1000000000).split("[.]")[0]));  // Changing it from 1000 to 1
                    if (str) {
                        result+="G";
                    }
                    return result;
                } else {
                    return removeDecimal(num); // Keeping it 1000 and not 1, but without the .0
                }
            } else { // Decimal IS NOT .0
                result = String.valueOf(num/1000000000);
                if (str) {
                    result+="G";
                }
                return result;
            }
        } else {
            /*
            * Shouldn't happen, means it's either negative, or bigger than 1 billion
            * - which makes no sense, in this software.
            * Returning the number as it is.
            * */
            return String.valueOf(num);
        }
    }

}