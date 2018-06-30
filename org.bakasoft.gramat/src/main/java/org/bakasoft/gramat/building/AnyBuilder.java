package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.CharPredicateExpression;

public class AnyBuilder extends ExpressionBuilder {

	@Override
	public Expression generateExpression(GrammarBuilder grammarBuilder) {
		return new CharPredicateExpression("any", c -> true);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		return this;
	}

	@Override
	public AnyBuilder clone() {
		return new AnyBuilder();
	}
}
