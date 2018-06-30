package org.bakasoft.gramat.io;

import java.io.PrintWriter;
import java.io.Writer;

import org.bakasoft.gramat.Expression;

public class GramatWriter extends PrintWriter {

	public GramatWriter(Writer writer) {
		super(writer);
	}
	
	public void writeName(String name) {
		writeDelimitedString(name, '`');
	}

	public void writeDelimitedString(String value, char delimiter) {
		write(delimiter);
		write(value);
		write(delimiter);
	}

	public void write(Expression expression) {
		expression.toString(this);
	}

}
