package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

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
}
