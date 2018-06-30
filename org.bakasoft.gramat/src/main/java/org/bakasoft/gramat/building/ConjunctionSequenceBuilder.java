package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;

public class ConjunctionSequenceBuilder extends ExpressionBuilder {

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		Expression[] expressions = getChildren().stream()
				.map(item -> grammarBuilder.build(item))
				.toArray(Expression[]::new);
		
		if (expressions.length == 0) {
			throw new RuntimeException(); // TODO empty expression error
		} else if (expressions.length == 1) {
			return expressions[0];
		}
		
		return new ConjunctionSequence(expressions);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (getChildren().isEmpty()) {
			throw new RuntimeException(); // TODO empty list
		}
		
		return getChildren().get(0).getStartExpression(grammarBuilder);
	}

	@Override
	public ExpressionBuilder clone() {
		ConjunctionSequenceBuilder clone = new ConjunctionSequenceBuilder();
		
		cloneChildrenInto(clone);
		
		return clone;
	}

}
