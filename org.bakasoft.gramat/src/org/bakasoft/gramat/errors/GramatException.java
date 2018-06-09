package org.bakasoft.gramat.errors;

abstract public class GramatException extends RuntimeException {

	private static final long serialVersionUID = 8233361969399676175L;

	public GramatException(String message) {
		super(message);
	}
	
	public GramatException(String message, Throwable cause) {
		super(message, cause);
	}
}
