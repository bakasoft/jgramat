package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

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

	@Override
	public int hashCode() {
		return CompareTool.hashMix(expression, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		Complement other = CompareTool.checkType(obj, Complement.class);
		
		return other != null
				&& Objects.equals(this.expression, other.expression);
	}

}
