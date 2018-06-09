package org.bakasoft.gramat.util;

public class TextPosition {

	private final int line;
	private final int column;
	
	public TextPosition(int line, int column) { 
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
	
	@Override
	public String toString() {
		return "line " + line + ", column " + column;
	}
	
}
