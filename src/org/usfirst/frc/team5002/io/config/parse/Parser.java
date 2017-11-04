/*
 * Copyright (c) 2017 Brandon Gong
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.usfirst.frc.team5002.io.config.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Parser.java : Parses and tokenizes PML configuration files.
 *
 * Currently, this is still a very experimental parser.  There is still much
 * work to be done before I consider it to have reached 1.0.0 maturity.
 * For example, it should make use of the ConfigParseException class more often.
 * (As of now, anything is valid PML syntax, and this means an error in the
 * configuration string leads to not an exception but instead unwarned,
 * undefined behavior.)
 *
 * I'm sure there's lots of other optimizations that can be made. The main goal
 * is to keep the parser as small and simple as possible (it currently sits at
 * around 100ish lines of code, clean and well-commented).  This is done at the
 * expense of the flexibility of the parser.  But that's ok.
 *
 * TODO: make the parser complain when you throw broken and disgusting things
 * at it.
 *
 * @author Brandon Gong
 * @version 0.1.0
 * Date: 11.3.17
 */
class Parser {

    /**
     * Main (and currently only) entry point to the parser.
     * @param pml the complete preprocessed markup file.
     * @return a flat list of string arrays of tokens.
     */
    public static List<ArrayList<String>> parse(String pml) {

        // initialize the empty result list.
        List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        /*
         * split the input up into blocks by semicolons.
         * Each PML statement is terminated by a semicolon, so after this step
         * we'll have an array of statements.
         */
        String[] blocks = pml.split(";");

        // define and initialize some fields.
        String device = "";
        String input = "";
        String filter = "changed";

        // iterate through each statement.
        for(String block : blocks) {

            // we split each statement up by the pipe character.
            String[] subBlocks = block.split("\\|");

            // we have to parse the first subBlock a little differently than
            // all the other ones.  So we do it outside the loop.
            device = subBlocks[0].substring(0, subBlocks[0].indexOf('.'));
            input = subBlocks[0].substring(
                subBlocks[0].indexOf('.') + 1,
                subBlocks[0].length()
            ).split("::")[0];
            filter = subBlocks[0].contains("::")
                ? subBlocks[0].split("::")[1]
                : filter;

            // args is an arraylist containing all of the pipeline steps.
            // we initialize it so we can use it later on outside of the loop.
            List<String> args = null;

            // iterate through subBlocks
            for(int i = 1; i < subBlocks.length; i++) {

                // if the index of this subBlock is odd, we are in a pipeline.
                // we parse it differently otherwise.
                if(i % 2 == 1) {
                    args = new ArrayList<String>(
                        Arrays.asList(subBlocks[i].split("->"))
                    );
                    // this line shows up quite a bit throughout the parser;
                    // it simply removes empty strings from the list that are
                    // created using the .split() method.
                    args.removeAll(Arrays.asList("", null));

                } else {
                    // if the subBlock index is even, we are in a configuration
                    // block.
                    String[] parts = subBlocks[i].split(",");

                    // temporary arraylist to compose all of the fields we have
                    // so far into a single arraylist to add to the result
                    // value.
                    ArrayList<String> comp = new ArrayList<>();
                    Collections.addAll(comp, device, input, filter);
                    comp.addAll(args);
                    comp.add(parts[0]);
                    result.add(comp);

                    // if there's only one part to this block, we are at the
                    // end of a pipeline; continue to the next statement.
                    if(parts.length == 1) continue;

                    // otherwise, parse out the other fields and change them
                    // accordingly.
                    else {
                        String subParts[] = parts[1].split("::");

                        // If subParts[0] (the input name) is not empty, that
                        // means we have changed to a new input, so change
                        // the filter field back to its default value and
                        // update the input field.
                        if(!(subParts[0] == null || subParts[0].equals(""))) {
                            input = subParts[0]
                                .substring(1, subParts[0].length());
                            filter = "changed";
                        }

                        // if there are two subParts, then a filter is also
                        // defined, so we parse it out.
                        if(subParts.length == 2) {
                            filter =
                                (subParts[1] == null || subParts[1].equals(""))
                                ? filter
                                : subParts[1];
                        }
                    }
                }
            }
        }
        return result;
    }
}
