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

/**
 * ConfigInterpreter can be viewed as a decorator class for Parser; it adds on a
 * preprocessor which accepts config line-by-line, and also adds on an
 * interpret method which searches through hashmaps based on the return value
 * of the parser and automatically builds the pipelines.
 *
 * @author Brandon Gong
 * @version 1.0.0
 * Date: 11.3.17
 */
public class ConfigInterpreter {

    // Preprocessor container string.
    private static String preprocessed = "";

    /**
     * Read and preprocess the next line.
     * @param line The line of config to read.
     */
    public static void readNextLine(String line) {

        // Remove all comments and whitespace.
        line = line.replaceAll("\\s", "");
        int commentBlockIndex = line.indexOf('#');

        // TODO: substring to the next newline, not the end of the line.
        preprocessed += (commentBlockIndex != -1)
            ? line.substring(0, commentBlockIndex)
            : line;
    }

    // TODO: implement.
    public static void interpret() {
        System.out.println(Parser.parse(preprocessed).toString());
        preprocessed = "";
    }
}
