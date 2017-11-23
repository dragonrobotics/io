import wpilib

class LogitechF310:

    def __init__(self, x): self.joystick = wpilib.Joystick(x)

    # TODO: fix ports & reorder accordingly.
    def x                     (self) : return self.joystick.getRawButton(1)
    def y                     (self) : return self.joystick.getRawButton(2)
    def a                     (self) : return self.joystick.getRawButton(3)
    def b                     (self) : return self.joystick.getRawButton(4)
    def dpad_up               (self) : return self.joystick.getRawButton(5)
    def dpad_down             (self) : return self.joystick.getRawButton(6)
    def dpad_left             (self) : return self.joystick.getRawButton(7)
    def dpad_right            (self) : return self.joystick.getRawButton(8)
    def left_trigger          (self) : return self.joystick.getRawButton(9)
    def right_trigger         (self) : return self.joystick.getRawButton(10)
    def left_joystick_button  (self) : return self.joystick.getRawButton(11)
    def right_joystick_button (self) : return self.joystick.getRawButton(12)
    def back                  (self) : return self.joystick.getRawButton(13)
    def start                 (self) : return self.joystick.getRawButton(14)

    def left_trigger          (self) : return self.joystick.getRawAxis(0)
    def right_trigger         (self) : return self.joystick.getRawAxis(1)
    def left_joystick_x       (self) : return self.joystick.getRawAxis(2)
    def left_joystick_y       (self) : return self.joystick.getRawAxis(3)
    def right_joystick_x      (self) : return self.joystick.getRawAxis(4)
    def right_joystick_y      (self) : return self.joystick.getRawAxis(5)

class LogitechExtreme3DPro:

    def __init__(self, x): self.joystick = wpilib.Joystick(x)

    # TODO: implement functions for inputs.
