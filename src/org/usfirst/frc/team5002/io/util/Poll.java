package org.usfirst.frc.team5002.io.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.usfirst.frc.team5002.io.input.base.Input;

public class Poll {
	
	private static ArrayList<Input<?, ?>> inputs;
	private static ArrayList<Object> values;
	
	public static void poll() {
		for(int i = 0; i < inputs.size(); i++) {
			
			// TODO: I doubt this .equals() even works
			if(inputs.get(i).getRawValue().equals(values.get(i))) continue;
			
			
		}
	}

	public static void add(HashMap<String, Input<?, ?>> hashmap) {
		inputs.addAll(hashmap.values());
		values = new ArrayList<Object>(inputs.size());
	}

}
