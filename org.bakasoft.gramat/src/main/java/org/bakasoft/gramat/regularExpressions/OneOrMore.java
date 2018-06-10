package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class OneOrMore extends Expression {

	private final Expression expr;
	
	public OneOrMore(Grammar grammar, Expression expr) {
		super(grammar);
		this.expr = expr;
	}

	@Override
	public boolean process(Context context) {
//		int position = context.tape.getPosition();
		
		if (context.process(expr)) {
			do {
//				position = context.tape.getPosition();
			} while(context.process(expr));
			
//			context.tape.setPosition(position);
			return true;
		}
		
//		context.tape.setPosition(position);
		return false;
	}
	
	public Expression getExpression() {
		return expr;
	}

}
