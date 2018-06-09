package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class DisjunctionSequence extends Expression {

	private final Expression[] expressions;
	
	public DisjunctionSequence(Grammar grammar, Expression[] expressions) {
		super(grammar);
		
		this.expressions = expressions;
	}

	@Override
	public boolean process(Context context) {
		boolean result = true;
		int position = context.tape.getPosition();
		
		for (int i = 0; i < expressions.length; i++) {
			if (context.process(expressions[i])) {
				return true;
			} else {
				context.tape.setPosition(position);
				result = false;
			}
 		}
		
		return result;
	}
	
	public Expression[] getExpressions() {
		return expressions;
	}
	
}
