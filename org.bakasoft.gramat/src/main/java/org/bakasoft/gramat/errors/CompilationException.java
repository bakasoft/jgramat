package org.bakasoft.gramat.errors;

import org.bakasoft.gramat.util.TextPosition;

public class CompilationException extends GramatException {

	private static final long serialVersionUID = 96681366539041986L;

	public CompilationException(Exception cause, TextPosition position) {
		super("error at " + position, cause);
	}

}
