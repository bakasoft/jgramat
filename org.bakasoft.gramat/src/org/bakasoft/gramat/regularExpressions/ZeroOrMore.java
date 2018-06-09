package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class ZeroOrMore extends Expression {

	private final Expression expr;
	
	public ZeroOrMore(Grammar grammar, Expression expr) {
		super(grammar);
		this.expr = expr;
	}

	@Override
	public boolean process(Context context) {
//		int position;
		
		do {
//			position = context.tape.getPosition();
		} while(context.process(expr));
		
//		context.tape.setPosition(position);
		return true;
	}
	
	public Expression getExpression() {
		return expr;
	}

}
