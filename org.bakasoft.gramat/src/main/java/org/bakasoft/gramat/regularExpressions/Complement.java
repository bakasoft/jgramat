package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class Complement extends Expression {

	private final Expression expression;
	
	public Complement(Expression expression) {
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

	@Override
	public void toString(GramatWriter writer) {
		writer.write("(!");
		writer.write(expression);
		writer.write(")");
	}

}
