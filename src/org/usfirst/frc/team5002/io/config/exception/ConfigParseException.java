package org.usfirst.frc.team5002.io.config.exception;

public class ConfigParseException extends Exception {
	
	public ConfigParseException() {
		  super();
	  }
	  public ConfigParseException(String message) {
		  super(message);
	  }
	  public ConfigParseException(String message, Throwable cause) {
		  super(message, cause);
	  }
	  public ConfigParseException(Throwable cause) {
		  super(cause);
	  }

}
