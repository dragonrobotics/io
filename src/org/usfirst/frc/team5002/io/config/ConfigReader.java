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
