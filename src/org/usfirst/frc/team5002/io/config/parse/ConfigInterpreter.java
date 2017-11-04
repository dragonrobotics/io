package org.usfirst.frc.team5002.io.config.parse;

public class ConfigInterpreter {
	
	public static String preprocessed = "";
	
	public static void readNextLine(String line) {
		line = line.replaceAll("\\s", "");
		int commentBlockIndex = line.indexOf('#');
		preprocessed += (commentBlockIndex != -1) ? line.substring(0, commentBlockIndex) : line;
	}
	
	public static void interpret() {
		System.out.println(Parser.parse(preprocessed).toString());
		preprocessed = "";
	}

}
