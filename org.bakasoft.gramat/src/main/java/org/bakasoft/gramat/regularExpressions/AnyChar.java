package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class AnyChar extends Expression {

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof AnyChar;
	}

	@Override
	public boolean process(Context context) {
		if (context.tape.isOpen()) {
			context.tape.consume();
			return true;
		}
		
		return false;
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.append('.');
	}

}
