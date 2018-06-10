package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class Complement extends Expression {

	private final Expression expression;
	
	public Complement(Grammar grammar, Expression expression) {
		super(grammar);
		this.expression = expression;
	}

	@Override
	public boolean process(Context context) {
		int position = context.tape.getPosition();
		
		if (context.process(expression)) {
			context.tape.setPosition(position);
			return false;
		}
		
		context.tape.consume();
		return true;
	}
	
	public Expression getExpression() {
		return expression;
	}

}
