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