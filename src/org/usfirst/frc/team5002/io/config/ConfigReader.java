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

public class ConfigReader {

    private ConfigReader() {}

    public static void initializeFromFile(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(ConfigInterpreter::readNextLine);
            ConfigInterpreter.interpret();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeFromString(String input) {
        String[] lines = input.split("\n");
        for(String line : lines) {
            ConfigInterpreter.readNextLine(line);
        }
        ConfigInterpreter.interpret();
    }

    public static void main(String[] args) {
        initializeFromString("                                                             \n"
            + "gamepad1                                                                    \n"
            + "    .left_joystick_x                                                        \n"
            + "            :: always |-> foc -> deadband(0.5) ->| strafe,                     \n"
            + "                      |->| log,                                                \n"
            + "    .left_joystick_y                                                        \n"
            + "            |-> foc-> | forward                                                \n"
            + "       ;                                                                       \n"
            + "                                                                            \n"
            + "# Comment line                                                              \n"
            + "navx.heading                                                                \n"
            + "    :: sometimes |-> smoothing -> scaling(5, 6,7, 8) -> asdf ->| value,     \n"
            + "    :: always |->| log # comments are allowed on the same line as well      \n"
            + "       ;                                                                       \n"

        );
        initializeFromFile("test.pml");
    }

}
