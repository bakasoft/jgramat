package org.bakasoft.gramat.regularExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;
import org.bakasoft.gramat.util.CharPredicate;
import org.bakasoft.gramat.util.CompareTool;

public class CharPredicateExpression extends Expression {

	private final String name;
	private final CharPredicate predicate;
	
	public CharPredicateExpression(String name, CharPredicate predicate) {
		this.name = name;
		this.predicate = predicate;
	}

	public CharPredicate getPredicate() {
		return predicate;
	}

	@Override
	public boolean process(Context context) {
		if (context.tape.isOpen() && predicate.test(context.tape.peek())) {
			context.tape.consume();
			return true;
		}
		
		return false;
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.writeName(name);
	}

	@Override
	public int hashCode() {
		return CompareTool.hashMix(name, predicate, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		CharPredicateExpression other = CompareTool.checkType(obj, CharPredicateExpression.class);
		
		return other != null
				&& Objects.equals(this.name, other.name)
				&& Objects.equals(this.predicate, other.predicate);
	}
	
}
