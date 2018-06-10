package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class ConjunctionSequence extends Expression {

	private final Expression[] expressions;
	
	public ConjunctionSequence(Grammar grammar, Expression[] expressions) {
		super(grammar);
		
		this.expressions = expressions;
	}

	@Override
	public boolean process(Context context) {
//		int position = context.tape.getPosition();
		
		for (int i = 0; i < expressions.length; i++) {
			if (!context.process(expressions[i])) {
//				context.tape.setPosition(position);
				return false;
			}
 		}
		
		return true;
	}
	
	public Expression[] getExpressions() {
		return expressions;
	}

}
