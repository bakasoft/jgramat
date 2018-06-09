package org.bakasoft.gramat.errors;

public class IdentifierNotAvailableException extends GramatException {

	private static final long serialVersionUID = 277909750830868886L;

	public IdentifierNotAvailableException(String id) {
		super(id); // TODO descriptive message
	}
	
}
