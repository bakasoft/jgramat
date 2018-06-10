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
	
	default boolean is(char c) {
		return c == peek();
	}

	default boolean isNot(char c) {
		return c != peek();
	}
	
	default boolean is(CharPredicate predicate) {
		return predicate.test(peek());
	}
	
	default boolean isNot(CharPredicate predicate) {
		return !predicate.test(peek());
	}

	default void expect(char c) {
		if (c != peek()) {
			throw new RuntimeException("expected " + c);
		}
		
		consume();
	}
	
}
