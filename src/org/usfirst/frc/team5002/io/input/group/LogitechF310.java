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

package org.usfirst.frc.team5002.io.input.group;

import org.usfirst.frc.team5002.io.input.base.Input;
import edu.wpi.first.wpilibj.Joystick;

public class LogitechF310 extends InputGroup<Joystick> {

    public LogitechF310(Joystick joystick) {
        super(joystick);
    }

    // TODO: FIX NUMBERS ASAP
    @Override
    public void initMap() {
        this.add("left_joystick_x",       new Input<Double, Double>(   () -> device.getRawAxis(0)    ));
        this.add("left_joystick_y",       new Input<Double, Double>(   () -> device.getRawAxis(1)    ));
        this.add("right_joystick_x",      new Input<Double, Double>(   () -> device.getRawAxis(2)    ));
        this.add("right_joystick_y",      new Input<Double, Double>(   () -> device.getRawAxis(3)    ));
        this.add("left_trigger",          new Input<Double, Double>(   () -> device.getRawAxis(4)    ));
        this.add("right_trigger",         new Input<Double, Double>(   () -> device.getRawAxis(5)    ));

        this.add("left_joystick_button",  new Input<Boolean, Boolean>( () -> device.getRawButton(0)  ));
        this.add("right_joystick_button", new Input<Boolean, Boolean>( () -> device.getRawButton(1)  ));
        this.add("dpad_up",               new Input<Boolean, Boolean>( () -> device.getRawButton(2)  ));
        this.add("dpad_down",             new Input<Boolean, Boolean>( () -> device.getRawButton(3)  ));
        this.add("dpad_left",             new Input<Boolean, Boolean>( () -> device.getRawButton(4)  ));
        this.add("dpad_right",            new Input<Boolean, Boolean>( () -> device.getRawButton(5)  ));
        this.add("a",                     new Input<Boolean, Boolean>( () -> device.getRawButton(6)  ));
        this.add("b",                     new Input<Boolean, Boolean>( () -> device.getRawButton(7)  ));
        this.add("x",                     new Input<Boolean, Boolean>( () -> device.getRawButton(8)  ));
        this.add("y",                     new Input<Boolean, Boolean>( () -> device.getRawButton(9)  ));
        this.add("left_bumper",           new Input<Boolean, Boolean>( () -> device.getRawButton(10) ));
        this.add("right_bumper",          new Input<Boolean, Boolean>( () -> device.getRawButton(11) ));
        this.add("back",                  new Input<Boolean, Boolean>( () -> device.getRawButton(12) ));
        this.add("start",                 new Input<Boolean, Boolean>( () -> device.getRawButton(13) ));
    }

}
