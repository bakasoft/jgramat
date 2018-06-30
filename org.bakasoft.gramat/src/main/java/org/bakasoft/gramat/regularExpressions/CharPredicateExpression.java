package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CharPredicate;

public class CharPredicateExpression extends Expression {

	private final String name;
	private final CharPredicate predicate;
	
	public CharPredicateExpression(String name, CharPredicate predicate) {
		this.name = name;
		this.predicate = predicate;
	}

	public CharPredicate getPredicate() {
		return predicate;
	}

	@Override
	public boolean process(Context context) {
		if (context.tape.isOpen() && predicate.test(context.tape.peek())) {
			context.tape.consume();
			return true;
		}
		
		return false;
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.writeName(name);
	}
	
}
