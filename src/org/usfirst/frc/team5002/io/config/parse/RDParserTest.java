/**
 * @file RDParserTest.java
 * Implements (non-automated) testing for the recursive descent PML parser.
 *
 * @author Sebastian Mobo
 * @version 0.1.0
 * @date 04-Nov-2017
 */

package org.usfirst.frc.team5002.io.config.parse;

import java.util.regex.Pattern;
import org.usfirst.frc.team5002.io.config.parse.RecursiveDescentParser;
import org.usfirst.frc.team5002.io.config.exception.ConfigParseException;

/**
 * Simple class for testing the recursive descent parser.
 * @see RecursiveDescentParser.java
 * @author Sebastian Mobo
 * @version 0.1.0
 * @date 04-Nov-2017
 */
class RDParserTest {
    private static void testOutput(String input, boolean status, boolean exp) {
        System.out.println(
            input
            + "\n    parse status: " + String.valueOf(status)
            + " (should be "+String.valueOf(exp)+")"
        );
    }

    private static void testOutput(
        String input, ConfigParseException e, boolean exp)
    {
        System.out.println(
            input
            + "\n    parse status: syntax error - "
            + e.getMessage()
            + " (should be "+String.valueOf(exp)+")"
        );
    }

    private static void syntaxErrOutput(String input, boolean status) {
        System.out.println(
            input
            + "\n    parse status: " + String.valueOf(status)
            + " (should be a syntax error)"
        );
    }

    private static void syntaxErrOutput(String input, ConfigParseException e) {
        System.out.println(
            input
            + "\n    parse status: syntax error - "
            + e.getMessage()
            + " (should be a syntax error)"
        );
    }

    public static void main(String[] args) {
        String parserTest1 = "gamepad1.left_joystick_y :: always  |-> foc ->| forward;";
        String parserTest2 = "10";
        String parserTest3 = "this.causes :: an |-> error";

        try {
            boolean test1 = RecursiveDescentParser.parse(parserTest1);
            testOutput(parserTest1, test1, true);
        } catch(ConfigParseException e) {
            testOutput(parserTest1, e, true);
        }

        try {
            boolean test2 = RecursiveDescentParser.parse(parserTest2);
            testOutput(parserTest2, test2, false);
        } catch(ConfigParseException e) {
            testOutput(parserTest2, e, false);
        }

        try {
            boolean test3 = RecursiveDescentParser.parse(parserTest3);
            syntaxErrOutput(parserTest3, test3);
        } catch(ConfigParseException e) {
            syntaxErrOutput(parserTest3, e);
        }
    }
}
