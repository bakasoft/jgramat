package org.bakasoft.gramat.util;

public class StringTape implements Tape {

	private final char[] chars;
	
	private int position;
	
	public StringTape(String value) {
		chars = value.toCharArray();
		position = 0;
	}
	
	@Override
	public boolean isOpen() {
		return position < chars.length;
	}

	@Override
	public char peek() {
		if (position >= chars.length) {
			throw new RuntimeException();
		}
		
		return chars[position];
	}

	@Override
	public void consume() {
		if (position >= chars.length) {
			throw new RuntimeException();
		}
		
		position++;
	}

	@Override
	public boolean isClosed() {
		return position >= chars.length;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public void setPosition(int position) {
		if (position < 0 || position > chars.length) {
			throw new RuntimeException();
		}
		
		this.position = position;
	}

	@Override
	public String substring(int startIndex, int endIndex) {
		int offset = startIndex;
		int count = endIndex - startIndex;
		
		if (offset < 0) { offset = 0; }
		else if (offset > chars.length) { offset = chars.length; }
		
		if (count < 0) { count = 0; }
		if (offset + count >= chars.length) { count = chars.length - offset; }
		
		return new String(chars, offset, count);
	}

	@Override
	public TextPosition getTextPosition() {
		int line = 1;
		int column = 1;
		
		for (int i = 0; i < position; i++) {
			if (i < chars.length) {
				char c = chars[i];
				
				if (c == '\n') {
					line++;
					
					column = 1;
				} else if (c != '\r') {
					column++;
				}
			} else {
				column++;
			}	
		}
		
		return new TextPosition(line, column);
	}

	@Override
	public char charAt(int index) {
		return chars[index];
	}

	@Override
	public int getLength() {
		return chars.length;
	}
	
}
