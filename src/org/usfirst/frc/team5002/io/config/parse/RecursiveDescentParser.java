/**
 * @file RecursiveDescentParser.java
 * @brief Implements a recursive descent parser for the PML syntax.
 *
 * This file implements a
 * [recursive descent parser](https://en.wikipedia.org/wiki/Recursive_descent_parser)
 * for PML syntax, defined by the following EBNF:
 * ```ebnf
 * letter = "A" | "B" | "C" | "D" | "E" | "F" | "G"
 *       | "H" | "I" | "J" | "K" | "L" | "M" | "N"
 *       | "O" | "P" | "Q" | "R" | "S" | "T" | "U"
 *       | "V" | "W" | "X" | "Y" | "Z" | "a" | "b"
 *       | "c" | "d" | "e" | "f" | "g" | "h" | "i"
 *       | "j" | "k" | "l" | "m" | "n" | "o" | "p"
 *       | "q" | "r" | "s" | "t" | "u" | "v" | "w"
 *       | "x" | "y" | "z" ;
 * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
 * character = letter | digit | "_" | "-";
 *
 * ident_or_param = character, { character };
 *
 * parameter_set = '(', { ident_or_param, ',' }, ident_or_param, ')';
 * parameterized_identifier = ident_or_param, [parameter_set];
 *
 * pipeline_stage = '|->', parameterized_identifier;
 * pipeline_callback = '->|', ident_or_param;
 * pipeline_sequence = '|->|', pipeline_callback
 *                     | pipeline_stage, { pipeline_stage }, pipeline_callback;
 *
 * process = '::', [parameterized_identifier], pipeline_sequence;
 * process_set = process, {',', process};
 *
 * input = '.', ident_or_param, process_set;
 * input_set = input, {',', input};
 *
 * pipeline = ident_or_param, input_set;
 * ```
 *
 * @author Sebastian Mobo
 * @date 04-Nov-2017
 * @version 0.1.0
 */

package org.usfirst.frc.team5002.io.config.parse;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import org.usfirst.frc.team5002.io.config.exception.ConfigParseException;


public class RecursiveDescentParser {
    //! Matches parameter and identifier values: a sequence of one or more
    //! letters / digits / hyphens / underscores.
    private static final Pattern parameter_identifier = Pattern.compile("[\\w[\\-]]+");

    //! Matches '|->' (token beginning pipeline stages)
    private static final Pattern stageBegin = Pattern.compile("\\|\\-\\>");

    //! Matches '->|' (token ending pipelines)
    private static final Pattern pipelineEnd = Pattern.compile("\\-\\>\\|");

    //! Matches '|->|' (token for empty pipelines)
    private static final Pattern pipelineIdentity = Pattern.compile("\\|\\-\\>\\|");

    //! Matches '::' (token in pipeline input specifiers)
    private static final Pattern doubleColon = Pattern.compile("\\:\\:");

    //! Matches whitespace
    private static final Pattern whitespace = Pattern.compile("\\s+");

    private String input;
    private Matcher matcher;

    //! Contains the last multicharacter token consumed via the consume(Pattern)
    //! method.
    //! lastConsumedToken.group() will get the actual matched token string.
    private MatchResult lastConsumedToken;

    public RecursiveDescentParser(String input) {
        this.input = input;
        this.matcher = identifier.matcher(input);
    }

    /**
     * Advances the matcher region past any whitespace found at the beginning
     * of the matcher region.
     */
    private void skipWhitespace() {
        if(matcher.regionStart() >= input.length()) {
            return;
        }

        matcher.usePattern(whitespace);
        if(matcher.lookingAt()) {
            matcher.region(matcher.end(), matcher.regionEnd());
        }
    }

    /**
     * Tests whether or not the next character in the matcher region matches
     * a single character.
     *
     * This method will advance the matcher region if it does find a match.
     *
     * @param characterToConsume character to find in the matcher region
     */
    private boolean consume(char characterToConsume) {
        skipWhitespace();

        if(matcher.regionStart() >= input.length()) {
            return false;
        }

        if(input.charAt(matcher.regionStart()) == characterToConsume) {
            matcher.region(matcher.regionStart()+1, matcher.regionEnd());
            return true;
        }
        return false;
    }

    /**
     * Attempts to find a pattern from the beginning of the matcher region.
     *
     * This method will advance the matcher region if it does find a match.
     */
    private boolean consume(Pattern tokenToConsume) {
        skipWhitespace();

        if(matcher.regionStart() >= input.length()) {
            return false;
        }

        matcher.usePattern(tokenToConsume);
        if(matcher.lookingAt()) {
            lastConsumedToken = matcher.toMatchResult();
            matcher.region(matcher.end(), matcher.regionEnd());

            return true;
        }

        return false;
    }

    /**
     * Attempts to match a parameter set.
     *
     * The rule defining parameter sets is:
     * `parameter_set = '(', { parameter, ',' }, parameter, ')';`
     *
     * @return true if this token can be found, false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    private boolean parameter_set() throws ConfigParseException {
        if(consume('(')) {
            while(consume(parameter_identifier)) {
                //! TODO: maybe we should save parameter values somewhere?
                if(consume(')')) {
                    return true;
                } else if(!consume(',')) {
                    throw new ConfigParseException("parameter_set: expected ','");
                }
            }
            throw new ConfigParseException(
                "parameter_set: expected at least one parameter"
            );
        } else {
            return false;
        }
    }

    /**
     * Attempts to match an identifier, possibly with parameters.
     *
     * The rule defining parameterized identifiers is:
     * `parameterized_identifier = identifier, [parameter_set];`
     *
     * @return true if this token can be found, false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    private boolean parameterized_identifier() throws ConfigParseException {
        if(consume(parameter_identifier)) {
            //! TODO: save identifier values (along with any parameters)
            //! somewhere, perhaps?

            parameter_set(); // optional; disregard return value for now
            return true;
        } else {
            return false;
        }
    }

    /**
     * Attempts to match a pipeline stage specifier.
     *
     * The rule defining pipeline stages is:
     * `pipeline_stage = '|->', parameterized_identifier;`
     *
     * @return true if this token can be found, false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    private boolean pipeline_stage() throws ConfigParseException {
        if(consume(stageBegin)) {
            if(!parameterized_identifier()) throw new ConfigParseException(
                "pipeline_stage: expected identifier"
            );

            //! TODO: save stage data somewhere?

            return true;
        } else {
            return false;
        }
    }

    /**
     * Attempts to match a pipeline callback specifier.
     *
     * The rule defining pipeline stages is:
     * `pipeline_callback = '->|', identifier;`
     *
     * @return true if this token can be found, false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    private boolean pipeline_callback() throws ConfigParseException {
        if(consume(pipelineEnd)) {
            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "pipeline_callback: expected identifier"
            );

            //! TODO: save pipeline callback name somewhere?

            return true;
        } else {
            return false;
        }
    }

    /**
     * Attempts to match a pipeline sequence.
     *
     * The rule defining pipeline sequences is:
     * `pipeline_sequence =
     *      '|->|', pipeline_callback
     *      | pipeline_stage, { pipeline_stage }, pipeline_callback;`
     */
    private boolean pipeline_sequence() throws ConfigParseException {
        if(consume(pipelineIdentity)) {
            if(!consume(pipeline_callback)) throw new ConfigParseException(
                "pipeline_sequence: expected pipeline callback specifier"
            );
        } else if(pipeline_stage()) {
            while(pipeline_stage()) {};

            if(!consume(pipeline_callback)) throw new ConfigParseException(
                "pipeline_sequence: expected pipeline callback specifier"
            );
        } else {
            return false;
        }
    }

    /**
     * Attempts to match a pipeline process specification.
     *
     * The rule defining these is:
     * `process = '::', [parameterized_identifier], pipeline_sequence;`.
     */
    private boolean process() throws ConfigParseException {
        if(consume(doubleColon)) {
            parameterized_identifier();

            if(!pipeline_sequence()) throw new ConfigParseException(
                "process: expected pipeline sequence"
            );

            return true;
        }

        return false;
    }

    /**
     * Attempts to match a pipeline process set.
     *
     * The rule defining these is:
     * `process_set = process, {',', process};`
     */
    private boolean process_set() throws ConfigParseException {
        if(process()) {
            while(consume(',')) {
                if(!process()) throw new ConfigParseException(
                    "process_set: expected pipeline process specification"
                );
            }

            return true;
        }

        return false;
    }

    private boolean input() throws ConfigParseException {
        if(consume('.')) {
            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "input: expected identifier"
            );

            if(!process_set) throw new ConfigParseException(
                "input: expected pipeline process specification set"
            );
        }

        return false;
    }

    private boolean input_set() throws ConfigParseException {
        if(input()) {
            while(consume(',')) {
                if(!input()) throw new ConfigParseException(
                    "input_set: expected pipeline input specification"
                );
            }

            return true;
        }

        return false;
    }

    /**
     * Attempts to match a pipeline definition.
     *
     * The rule defining pipeline definitions is:
     * `pipeline = input_specifier, { pipeline_stage }, pipeline_callback;`
     *
     * @return true if this token can be found, false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    private boolean pipeline() throws ConfigParseException {
        if(!consume(parameter_identifier)) {
            //! TODO: probably should save pipeline input device somewhere

            if(!input_set()) throw new ConfigParseException(
                "pipeline: expected pipeline input set"
            );

            return true;
        }

        return false;
    }

    /**
     * Attempts to parse a pipeline specification.
     *
     * @param input a string containing a pipeline specification to parse
     * @return true if the string contained a valid pipeline specification,
     * false otherwise
     * @throws ConfigParseException if a syntax error is encountered
     */
    public static boolean parse(String input) throws ConfigParseException {
        RecursiveDescentParser parser = new RecursiveDescentParser(input);
        return parser.pipeline();
    }
}
