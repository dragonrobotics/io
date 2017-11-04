package org.usfirst.frc.team5002.io.config.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Parser {
	
	public static List<ArrayList<String>> parse(String pml) {
		
		List<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
		String[] blocks = pml.split(";");
		String device = "";
		String input = "";
		String filter = "changed";
		for(String block : blocks) {
			
			// we have to parse the first block a little differently than all the other ones.
			String[] subBlocks = block.split("\\|");
			device = subBlocks[0].substring(0, subBlocks[0].indexOf('.'));
			input = subBlocks[0].substring(subBlocks[0].indexOf('.') + 1, subBlocks[0].length()).split("::")[0];
			filter = subBlocks[0].contains("::") ? subBlocks[0].split("::")[1] : filter;
			
			List<String> args = null;
			for(int i = 1; i < subBlocks.length; i++) {
				if(i % 2 == 1) {
					args = new ArrayList<String>(Arrays.asList(subBlocks[i].split("->")));
					args.removeAll(Arrays.asList("", null));
				} else {
					String[] parts = subBlocks[i].split(",");
					
					ArrayList<String> comp = new ArrayList<>();
					Collections.addAll(comp, device, input, filter);
					comp.addAll(args);
					comp.add(parts[0]);
					result.add(comp);
					
					if(parts.length == 1) continue;
					else {
						String subParts[] = parts[1].split("::");
						if(!(subParts[0] == null || subParts[0].equals(""))) {
							input = subParts[0].substring(1, subParts[0].length());
							filter = "changed";
						}
						if(subParts.length == 2) {
							filter = (subParts[1] == null || subParts[1].equals("")) ? filter : subParts[1];
						}
					}
				}
			}
		}
		
		return result;
	}
	
}
