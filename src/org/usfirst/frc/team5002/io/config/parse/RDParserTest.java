/**
 * @file RDParserTest.java
 * Implements (non-automated) testing for the recursive descent PML parser.
 *
 * @author Sebastian Mobo
 * @version 0.1.0
 * @date 04-Nov-2017
 */

package org.usfirst.frc.team5002.io.config.parse;

import java.util.ArrayList;
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
    private static void syntaxErrOutput(String input, ConfigParseException e) {
        System.out.println(
            input
            + "\n    parse status: syntax error: \n"
            + e.getMessage()
        );
    }

    private static void displayPipelineSpec(PipelineSpecification spec) {
        System.out.println("Device: " + spec.device());
        System.out.println("Input: " + spec.input());
        System.out.println("Filter: " + spec.filter());
        System.out.println("Stages:");
        for(ParameterizedIdentifier stage : spec.stages()) {
            System.out.println("    " + stage.toString());
        }
        System.out.println("Callback: " + spec.callback());
    }

    private static void parsingTest(String input) {
        try {
            ArrayList<PipelineSpecification> test
                = RecursiveDescentParser.parse(input);

            System.out.println("Parse of: ");
            System.out.println(input);
            System.out.println("--------------");
            int i=0;
            for (PipelineSpecification spec : test) {
                i++;
                System.out.println("Specification "+String.valueOf(i)+':');
                displayPipelineSpec(spec);
                System.out.println("--------------");
            }
        } catch(ConfigParseException e) {
            syntaxErrOutput(input, e);
            System.out.println("--------------");
        }
    }

    public static void main(String[] args) {
        String parserTest4 =
            "gamepad1.right_joystick_x\n"+
            "   :: always |->| logging, # note the comma at the end of this line.\n"+
            "   # and the semicolon terminating the whole statement.\n"+
            "   :: changed |-> deadband(0.01) |-> FOC ->| twist;\n";

        parsingTest("gamepad1.left_joystick_y :: always  |-> foc ->| forward;");
        parsingTest("10");
        parsingTest("this.causes :: an |-> error");
        parsingTest("this.however :: is -> not -> an ->| error");
        parsingTest(parserTest4);
        parsingTest("gamepad1.right_joystick_x :: |->| logging");
    }
}
