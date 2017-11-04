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

package org.usfirst.frc.team5002.io.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.usfirst.frc.team5002.io.config.parse.ConfigInterpreter;

/**
 * ConfigReader.java : The main entry point for all calls to the configuration
 * file.
 *
 * All other source code should not call anything else but the ConfigReader.
 * All other config classes are just implementation details and are not safe
 * to call directly.
 *
 * @author Brandon Gong
 * @version 1.0.0
 * Date: 11.3.17
 */
public class ConfigReader {

    // Block instantiation.
    private ConfigReader() {}

    /**
     * Read and interpret the configuration from a file, with the given
     * path.
     * @param filename the path to the file.
     */
    public static void initializeFromFile(String filename) {
        // stream file contents to the preprocessor line-by-line.
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ConfigInterpreter::readNextLine);
            ConfigInterpreter.interpret();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read and interpret the configuration from a hardcoded string.
     * Probably the easiest way to do things, but also loses the benefit
     * of reconfiguring without recompiling.
     * @param input the input configuration.
     */
    public static void initializeFromString(String input) {
        // split the input along newlines and pass it line-by-line to the
        // preprocessor.
        String[] lines = input.split("\n");
        for(String line : lines) {
            ConfigInterpreter.readNextLine(line);
        }
        ConfigInterpreter.interpret();
    }

    // TODO: Test method only. delete.
    public static void main(String[] args) {
        initializeFromString("                                                             \n"
            + "gamepad1                                                                    \n"
            + "    .left_joystick_x                                                        \n"
            + "         :: always |-> foc -> deadband(0.5) ->| strafe,                     \n"
            + "                   |->| log,                                                \n"
            + "    .left_joystick_y                                                        \n"
            + "         |-> foc-> | forward                                                \n"
            + "    ;                                                                       \n"
            + "                                                                            \n"
            + "# Comment line                                                              \n"
            + "navx.heading                                                                \n"
            + "    :: sometimes |-> smoothing -> scaling(5, 6,7, 8) -> asdf ->| value,     \n"
            + "    :: always |->| log # comments are allowed on the same line as well      \n"
            + "    ;                                                                       \n"
        );
        initializeFromFile("test.pml");
    }
}
