package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

public class ConjunctionSequence extends Expression {

	private final Expression[] expressions;
	
	public ConjunctionSequence(Expression[] expressions) {
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

	@Override
	public void toString(GramatWriter writer) {
		writer.write('(');
		for (int i = 0; i < expressions.length; i++) {
			if (i > 0) {
				writer.write(' ');
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
		ConjunctionSequence other = CompareTool.checkType(obj, ConjunctionSequence.class);
		
		return other != null
				&& Objects.deepEquals(this.expressions, other.expressions);
	}

}
