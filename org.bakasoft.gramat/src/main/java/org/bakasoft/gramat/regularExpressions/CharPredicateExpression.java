package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.util.CharPredicate;

public class CharPredicateExpression extends Expression {

	private final CharPredicate predicate;
	
	public CharPredicateExpression(Grammar grammar, CharPredicate predicate) {
		super(grammar);
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
	
}
