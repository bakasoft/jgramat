package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

public class DisjunctionSequence extends Expression {

	private final Expression[] expressions;
	
	public DisjunctionSequence(Expression[] expressions) {
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

	@Override
	public void toString(GramatWriter writer) {
		writer.write('(');
		for (int i = 0; i < expressions.length; i++) {
			if (i > 0) {
				writer.write('|');
			}
			
			writer.write(expressions[i]);
		}	
		writer.write(')');
	}

	@Override
	public int hashCode() {
		return CompareTool.deepHashMix(expressions, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		DisjunctionSequence other = CompareTool.checkType(obj, DisjunctionSequence.class);
		
		return other != null
				&& Objects.deepEquals(this.expressions, other.expressions);
	}
	
}
