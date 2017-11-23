# Copyright (c) 2017 Brandon Gong
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

## @package gamepads
# Some default gamepad callbacks.
#
# @author Brandon Gong


import wpilib

## Define inputs for the Logitech F310 gamepad.
#
# This class wraps all of the wpilib calls into functions so that they can
# be easily passed into the input poller.  Notice we specifically do not
# use the python Property decorator because we do not want that syntactic
# flexibility.
class LogitechF310:

    ## Initialize a new LogitechF310 gamepad with the given port.
    # @param port the port that the gamepad is on.
    def __init__(self, port): self.joystick = wpilib.Joystick(port)

    # TODO: fix ports & reorder accordingly.

    ## Get the value of the 'x' button on the controller.
    def x                     (self) : return self.joystick.getRawButton(1)

    ## Get the value of the 'y' button on the controller.
    def y                     (self) : return self.joystick.getRawButton(2)

    ## Get the value of the 'a' button on the controller.
    def a                     (self) : return self.joystick.getRawButton(3)

    ## Get the value of the 'b' button on the controller.
    def b                     (self) : return self.joystick.getRawButton(4)

    ## Get the value of the directional-pad up button on the controller.
    def dpad_up               (self) : return self.joystick.getRawButton(5)

    ## Get the value of the directional-pad down button on the controller.
    def dpad_down             (self) : return self.joystick.getRawButton(6)

    ## Get the value of the directional-pad left button on the controller.
    def dpad_left             (self) : return self.joystick.getRawButton(7)

    ## Get the value of the directional-pad right button on the controller.
    def dpad_right            (self) : return self.joystick.getRawButton(8)

    ## Get the value of the left bumper on the controller.
    def left_bumper           (self) : return self.joystick.getRawButton(9)

    ## Get the value of the right bumper on the controller.
    def right_bumper          (self) : return self.joystick.getRawButton(10)

    ## Get the value of the left joystick button on the controller.
    def left_joystick_button  (self) : return self.joystick.getRawButton(11)

    ## Get the value of the right joystick button on the controller.
    def right_joystick_button (self) : return self.joystick.getRawButton(12)

    ## Get the value of the back button on the controller.
    def back                  (self) : return self.joystick.getRawButton(13)

    ## Get the value of the start button on the controller.
    def start                 (self) : return self.joystick.getRawButton(14)

    ## Get the value of the left trigger on the controller.
    def left_trigger          (self) : return self.joystick.getRawAxis(0)

    ## Get the value of the right trigger on the controller.
    def right_trigger         (self) : return self.joystick.getRawAxis(1)

    ## Get the value of the x-axis of the left joystick on the controller.
    def left_joystick_x       (self) : return self.joystick.getRawAxis(2)

    ## Get the value of the y-axis of the left joystick on the controller.
    def left_joystick_y       (self) : return self.joystick.getRawAxis(3)

    ## Get the value of the x-axis of the right joystick on the controller.
    def right_joystick_x      (self) : return self.joystick.getRawAxis(4)

    ## Get the value of the y-axis of the right joystick on the controller.
    def right_joystick_y      (self) : return self.joystick.getRawAxis(5)

## Define inputs for the Logitech Extreme 3D Pro Joystick.
# Similar idea to class `LogitechF310`, we want to wrap everything in a callback
# so we can easily pass it to the `iota` poller.
#
# This class is currently unimplemented.
class LogitechExtreme3DPro:

    ## Initialize a new LogitechExtreme3DPro with the given port.
    # @param port the port that the joystick is on.
    def __init__(self, port): self.joystick = wpilib.Joystick(port)

    # TODO: implement functions for inputs.
