package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

public class ZeroOrMore extends Expression {

	private final Expression expr;
	
	public ZeroOrMore(Expression expr) {
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

	@Override
	public void toString(GramatWriter writer) {
		writer.write('(');
		writer.write(expr);
		writer.write(')');
		writer.write('*');
	}

	@Override
	public int hashCode() {
		return CompareTool.hashMix(expr, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		ZeroOrMore other = CompareTool.checkType(obj, ZeroOrMore.class);
		
		return other != null
				&& Objects.equals(this.expr, other.expr);
	}

}
