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
 * ident_or_param = character, { character } | digit, '.', {digit};
 *
 * parameter_set = '(', { ident_or_param, ',' }, ident_or_param, ')';
 * parameterized_identifier = ident_or_param, [parameter_set];
 *
 * pipeline_stage = '|->', parameterized_identifier;
 * pipeline_sequence = '|->|', ident_or_param
 *                   | pipeline_stage, { pipeline_stage }, '->|', pipeline_callback;
 *
 * process = '::', [parameterized_identifier], pipeline_sequence;
 * process_set = process, {',', process};
 *
 * input = '.', ident_or_param, process_set;
 * input_set = input, {',', input};
 *
 * pipeline = ident_or_param, input_set, ';';
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
import java.util.ArrayList;
import org.usfirst.frc.team5002.io.config.exception.ConfigParseException;


public class RecursiveDescentParser {
    //! Matches parameter and identifier values: a sequence of one or more
    //! letters / digits / hyphens / underscores.
    //! It also matches floating point numbers, which is a bit of a hack.
    private static final Pattern parameter_identifier =
        Pattern.compile("(?:\\-?\\d+(?:\\.\\d+)?)|[\\w[\\-]]+");

    //! Matches '|->' (token beginning pipeline stages)
    private static final Pattern stageBegin = Pattern.compile("\\|\\-\\>");

    //! Matches '->|' (token ending pipelines)
    private static final Pattern pipelineEnd = Pattern.compile("\\-\\>\\|");

    //! Matches '|->|' (token for empty pipelines)
    private static final Pattern pipelineIdentity = Pattern.compile("\\|\\-\\>\\|");

    //! Matches '::' (token in pipeline input specifiers)
    private static final Pattern doubleColon = Pattern.compile("\\:\\:");

    //! Matches whitespace and (possibly multiple lines of) comments
    private static final Pattern skipspace =
        Pattern.compile("(?:(?:#[^\\n]*\\n)|\\s)+");

    private String input;
    private Matcher matcher;

    //! Contains the last multicharacter token consumed via the consume(Pattern)
    //! method.
    //! lastConsumedToken.group() will get the actual matched token string.
    private MatchResult lastConsumedToken;

    public RecursiveDescentParser(String input) {
        this.input = input;
        this.matcher = parameter_identifier.matcher(input);
    }

    /**
     * Advances the matcher region past any whitespace or comments
     * found at the beginning of the matcher region.
     */
    private void skipWhitespace() {
        if(matcher.regionStart() >= input.length()) {
            return;
        }

        matcher.usePattern(skipspace);
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
     * @return an ArrayList of Strings containing the given parameters
     * (if any) in order; can be an empty list. Returns null if not matching.
     * @throws ConfigParseException if a syntax error is encountered
     */
    private ArrayList<String> parameter_set() throws ConfigParseException {
        if(consume('(')) {
            ArrayList<String> returnList = new ArrayList<String>();
            while(consume(parameter_identifier)) {
                returnList.add(lastConsumedToken.group());

                if(consume(')')) {
                    return returnList;
                } else if(!consume(',')) {
                    throw new ConfigParseException(
                        "parameter_set: expected ','"+parseErrorInfo()
                    );
                }
            }

            throw new ConfigParseException(
                "parameter_set: expected at least one parameter"
                +parseErrorInfo()
            );
        } else {
            return null;
        }
    }

    /**
     * Attempts to match an identifier, possibly with parameters.
     *
     * The rule defining parameterized identifiers is:
     * `parameterized_identifier = identifier, [parameter_set];`
     *
     * @return a ParameterizedIdentifier object containing info about the parsed
     * identifier / parameter set. Returns null if not matching.
     * @throws ConfigParseException if a syntax error is encountered
     */
    private ParameterizedIdentifier parameterized_identifier()
        throws ConfigParseException
    {
        if(consume(parameter_identifier)) {
            String identifier = lastConsumedToken.group();
            ArrayList<String> parameters = parameter_set();

            if(parameters == null) {
                parameters = new ArrayList<String>();
            }


            ParameterizedIdentifier data = new ParameterizedIdentifier(
                identifier, parameters
            );

            return data;
        } else {
            return null;
        }
    }

    /**
     * Attempts to match a pipeline stage specifier.
     *
     * The rule defining pipeline stages is:
     * `pipeline_stage = '|->', parameterized_identifier;`
     *
     * @return a ParameterizedIdentifier containing info on the parsed stage,
     * or null if not matched.
     * @throws ConfigParseException if a syntax error is encountered
     */
    private ParameterizedIdentifier pipeline_stage() throws ConfigParseException {
        if(consume(stageBegin)) {
            ParameterizedIdentifier retn = parameterized_identifier();
            if(retn == null) throw new ConfigParseException(
                "pipeline_stage: expected identifier"
                +parseErrorInfo()
            );

            return retn;
        } else {
            return null;
        }
    }

    /**
     * Attempts to match a pipeline callback specifier.
     *
     * The rule defining pipeline stages is:
     * `pipeline_callback = '->|', ident_or_param;`
     *
     * @return the callback identifier if matched; null otherwise.
     * @throws ConfigParseException if a syntax error is encountered
     */
    private String pipeline_callback() throws ConfigParseException {
        if(consume(pipelineEnd)) {
            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "pipeline_callback: expected identifier"
                +parseErrorInfo()
            );

            return lastConsumedToken.group();
        } else {
            return null;
        }
    }

    /**
     * Attempts to match a pipeline sequence.
     *
     * The rule defining pipeline sequences is:
     * `pipeline_sequence =
     *      '|->|', pipeline_callback
     *      | pipeline_stage, { pipeline_stage }, pipeline_callback;`
     * This method will also fill in the provided PipelineSpecification object
     * with information about the pipeline sequence.
     *
     * @param spec a PipelineSpecification to be filled in when parsing.
     * @return true if a pipeline sequence could be matched; false otherwise
     */
    private boolean pipeline_sequence(PipelineSpecification spec)
        throws ConfigParseException
    {
        if(consume(pipelineIdentity)) {
            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "pipeline_sequence: expected pipeline callback specifier for identity pipeline"
                +parseErrorInfo()
            );

            String callbackIdentifier = lastConsumedToken.group();

            spec.setStages(new ArrayList<ParameterizedIdentifier>());
            spec.setCallback(callbackIdentifier);

            return true;
        } else {
            ParameterizedIdentifier stageData = pipeline_stage();
            if(stageData == null) return false;

            ArrayList<ParameterizedIdentifier> stageList =
                new ArrayList<ParameterizedIdentifier>();

            stageList.add(stageData);
            while((stageData = pipeline_stage()) != null) {
                stageList.add(stageData);
            };


            if(!consume(pipelineEnd)) {
                throw new ConfigParseException(
                    "pipeline_sequence: expected '->|' after stages"
                    +parseErrorInfo()
                );
            }

            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "pipeline_sequence: expected pipeline callback specifier after stages"
                +parseErrorInfo()
            );

            String callback = lastConsumedToken.group();

            spec.setStages(stageList);
            spec.setCallback(callback);

            return true;
        }
    }

    /**
     * Attempts to match a pipeline process specification.
     *
     * This method will return a PipelineSpecification containing info about the
     * parsed specification, using the provided device and input names, and
     * potentially also using the provided default filter.
     *
     * The rule defining these is:
     * `process = '::', [parameterized_identifier], pipeline_sequence;`.
     *
     * @return a complete PipelineSpecification containing info about
     * the parsed specification
     * @param device the input device for the specification
     * @param input the input name for the specification
     * @param defaultFilter the filter to use for the specification (if one
     * is not provided in the specification itself)
     */
    private PipelineSpecification process(
        String device, String input, ParameterizedIdentifier defaultFilter)
        throws ConfigParseException
    {
        if(consume(doubleColon)) {
            ParameterizedIdentifier filter = parameterized_identifier();
            if(filter == null) filter = defaultFilter;

            PipelineSpecification spec = new PipelineSpecification(
                device, input, filter
            );

            if(!pipeline_sequence(spec)) throw new ConfigParseException(
                "process: expected pipeline sequence"
                +parseErrorInfo()
            );

            return spec;
        }

        return null;
    }

    /**
     * Attempts to match a pipeline process set.
     *
     * This method will _add_ the parsed PipelineSpecifications to the
     * given list.
     *
     * The rule defining these is:
     * `process_set = process, {',', process};`
     *
     * @param device the input device for specifications
     * @param input the input name for specifications
     * @param specList a list of PipelineSpecifications to add to
     * @return true if a process set could be matched; false otherwise
     */
    private boolean process_set(
        String device, String input, ArrayList<PipelineSpecification> specList
    ) throws ConfigParseException
    {
        ParameterizedIdentifier defaultFilter = new ParameterizedIdentifier(
            "change", new ArrayList<String>()
        );

        PipelineSpecification spec = process(device, input, defaultFilter);
        if(spec != null) {
            specList.add(spec);

            // chained pipeline specs reuse filters
            defaultFilter = spec.filter();

            while(consume(',')) {
                spec = process(device, input, defaultFilter);

                if(spec == null) {
                    throw new ConfigParseException(
                        "process_set: expected pipeline process specification"
                        +parseErrorInfo()
                    );
                }

                specList.add(spec);
                defaultFilter = spec.filter();
            }

            return true;
        }

        return false;
    }

    /**
     * Attempts to parse a single input specification.
     *
     * This method will _add_ any parsed PipelineSpecifications to the
     * given list.
     *
     * @param device the input device for specifications
     * @param specList a list of PipelineSpecifications to add to
     * @return true if an input could be matched; false otherwise
     */
    private boolean input(
        String device, ArrayList<PipelineSpecification> specList
    ) throws ConfigParseException {
        if(consume('.')) {
            if(!consume(parameter_identifier)) throw new ConfigParseException(
                "input: expected identifier"
                +parseErrorInfo()
            );

            String input = lastConsumedToken.group();

            if(!process_set(device, input, specList)) {
                throw new ConfigParseException(
                    "input: expected pipeline process specification set"
                    +parseErrorInfo()
                );
            }

            return true;
        }

        return false;
    }

    /**
     * Attempts to parse a set of input specifications.
     *
     * This method will _add_ any parsed PipelineSpecifications to the
     * given list.
     *
     * @param device the input device for specifications
     * @param specList a list of PipelineSpecifications to add to
     * @return true if an input set could be matched; false otherwise
     */
    private boolean input_set(
        String device, ArrayList<PipelineSpecification> specList
    ) throws ConfigParseException {
        if(input(device, specList)) {
            while(consume(',')) {
                if(!input(device, specList)) throw new ConfigParseException(
                    "input_set: expected pipeline input specification"
                    +parseErrorInfo()
                );
            }

            return true;
        }

        return false;
    }

    /**
     * Attempts to parse a single pipeline specification.
     *
     * @return a list of parsed PipelineSpecifications from the next parsed
     * pipeline specification, or null if one could not be parsed.
     * @throws ConfigParseException if a syntax error is encountered
     */
    private ArrayList<PipelineSpecification> pipeline()
        throws ConfigParseException
    {
        if(!consume(parameter_identifier)) {
            return null;
        }

        String device = lastConsumedToken.group();
        ArrayList<PipelineSpecification> specList
            = new ArrayList<PipelineSpecification>();

        if(!input_set(device, specList)) throw new ConfigParseException(
            "pipeline: expected pipeline input set"
            +parseErrorInfo()
        );

        if(consume(';') || matcher.regionStart() == input.length()) {
            return specList;
        } else {
            throw new ConfigParseException(
                "pipeline: expected terminating semicolon or EOF"
                +parseErrorInfo()
            );
        }
    }

    /**
     * Finds and dumps some basic info for debugging parse errors.
     *
     * This might be better off returning a structure containing the info
     * in a more machine-usable format or an object.
     */
    private String parseErrorInfo() {
        int currentAbsPos = matcher.regionStart();

        //! Dump rest of currently parsed line.
        matcher.usePattern(Pattern.compile("[^\\n]*\\n"));
        String restOfLine = "<empty>";

        if(matcher.lookingAt()) restOfLine = matcher.group();

        matcher.usePattern(Pattern.compile("(\\n)"));
        matcher.region(0, matcher.regionEnd());

        int lineNo;
        int linePos;
        String errorLine;

        if(matcher.find()) {
            lineNo = matcher.groupCount();
            linePos = matcher.start(matcher.groupCount()) - currentAbsPos;
            for(int i=0;i<matcher.groupCount()-1;i++) {
                int lineStartAbs = matcher.start(i);
                int lineEndAbs = matcher.start(i+1);

                if(lineStartAbs <= currentAbsPos && lineEndAbs >= currentAbsPos) {
                    lineNo = i;
                    linePos = lineStartAbs - currentAbsPos;
                    break;
                }
            }

            if(lineNo != matcher.groupCount()) {
                errorLine = input.substring(
                    matcher.start(lineNo),
                    matcher.start(lineNo+1)
                );
            } else {
                errorLine = input.substring(matcher.start(lineNo));
            }
        } else {
            // single-line
            lineNo = 0;
            linePos = currentAbsPos;
            errorLine = input;
        }



        StringBuilder b = new StringBuilder();
        b.append("\nat line ")
        .append(lineNo)
        .append(':')
        .append(linePos)
        .append(" : \n")
        .append(errorLine)
        .append('\n');

        // fill with spaces to linePos
        for(int i=0;i<linePos-1;i++) b.append(' ');
        b.append("^");

        return b.toString();
    }

    /**
     * Attempts to parse a set of pipeline specifications.
     *
     * @param input a string containing pipeline specifications to parse
     * @return a list of PipelineSpecifications from all parsed specifications
     * in the string.
     * @throws ConfigParseException if a syntax error is encountered
     */
    public static ArrayList<PipelineSpecification> parse(String input)
        throws ConfigParseException
    {
        RecursiveDescentParser parser = new RecursiveDescentParser(input);
        ArrayList<PipelineSpecification> specList
            = new ArrayList<PipelineSpecification>();

        ArrayList<PipelineSpecification> subList;
        while((subList = parser.pipeline()) != null) {
            specList.addAll(subList);
        }

        return specList;
    }
}
