package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class FuzzyString extends Expression {
	
	// add settings
	
	private final char[] chars;

	public FuzzyString(String value) {
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
				
				char actual = chars[i];
				char expected = context.tape.peek();
				
				if (actual != expected) { // TODO fuzzy match
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
		
		writer.writeDelimitedString(value, '~');
	}
}
