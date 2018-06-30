package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

public class StrictString extends Expression {

	private final char[] chars;

	public StrictString(char[] chars) {
		this.chars = chars;
	}
	
	public StrictString(String value) {
		chars = value.toCharArray();
	}

	@Override
	public boolean process(Context context) {
		if (chars.length > 0) {
//			int position = context.tape.getPosition();
			
			for (int i = 0; i < chars.length; i++) {
				if (context.tape.isClosed()) {
//					context.tape.setPosition(position);
					return false;
				}
				
				char actual = context.tape.peek();
				char expected = chars[i];
				
				if (actual != expected) {
//					context.tape.setPosition(position);
					return false;
				}
				
				context.tape.consume();
			}
		
			return true;
		}
		
		return false;
	}
	
	public String getValue() {
		return new String(chars);
	}

	@Override
	public void toString(GramatWriter writer) {
		String value = new String(chars);
		
		writer.writeDelimitedString(value, '"');
	}

	@Override
	public int hashCode() {
		return CompareTool.hashMix(new String(chars), getClass());
	}

	@Override
	public boolean equals(Object obj) {
		StrictString other = CompareTool.checkType(obj, StrictString.class);
		
		return other != null
				&& Objects.deepEquals(this.chars, other.chars);
	}
}
