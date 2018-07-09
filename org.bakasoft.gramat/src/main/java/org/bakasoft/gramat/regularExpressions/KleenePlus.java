package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CompareTool;

public class KleenePlus extends Expression {

	private final Expression expr;
	
	public KleenePlus(Expression expr) {
		this.expr = expr;
	}

	@Override
	public boolean process(Context context) {
		int initPosition = context.tape.getPosition();
		int prevPosition = -1; 
		int currPosition;
		int hits = 0;
		
		while(context.tape.isOpen()) {
			currPosition = context.tape.getPosition();
					
			if(context.process(expr)) {
				prevPosition = currPosition;
				currPosition = context.tape.getPosition();
				
				context.tape.setPosition(prevPosition);
				context.tape.consume();
				hits++;
			} else {
				context.tape.setPosition(currPosition);
				context.tape.consume();
				hits++;	
			}			
		}
		
		if (prevPosition != -1) {
			context.tape.setPosition(prevPosition);
		}

		if (prevPosition != -1 && prevPosition == initPosition) {
			return false;
		}
		
		return hits >= 1;
	}
	
	public Expression getExpression() {
		return expr;
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.write('(');
		writer.write(expr);
		writer.write(')');
		
		// TODO: the string representation can be simplified by using `.`
		
		writer.write('+');
	}

	@Override
	public int hashCode() {
		return CompareTool.hashMix(expr, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		KleenePlus other = CompareTool.checkType(obj, KleenePlus.class);
		
		return other != null
				&& Objects.equals(this.expr, other.expr);
	}

}
