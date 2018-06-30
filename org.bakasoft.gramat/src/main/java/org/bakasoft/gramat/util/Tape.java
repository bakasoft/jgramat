package org.bakasoft.gramat.util;

public interface Tape {

	boolean isOpen();
	
	boolean isClosed();
	
	char peek();
	
	void consume();
	
	int getPosition();
	
	void setPosition(int position);

	String substring(int startIndex, int endIndex);
	
	char charAt(int index);
	
	int getLength();
	
	TextPosition getTextPosition();
	
}
