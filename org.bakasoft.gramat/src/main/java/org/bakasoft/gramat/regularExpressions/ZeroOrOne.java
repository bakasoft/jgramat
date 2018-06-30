package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class ZeroOrOne extends Expression {

	private final Expression expr;
	
	public ZeroOrOne(Expression expr) {
		this.expr = expr;
	}

	@Override
	public boolean process(Context context) {
//		int position = context.tape.getPosition();
		
		if(!context.process(expr)) {
//			context.tape.setPosition(position);	
		}
		
		return true;
	}
	
	public Expression getExpression() {
		return expr;
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.write(expr);
		writer.write('?');
	}

}
