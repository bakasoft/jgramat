package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.CharPredicateExpression;
import org.bakasoft.gramat.util.CharPredicate;

public class PredicateBuilder extends ExpressionBuilder {

	private final String name;
	private final CharPredicate predicate;
	
	public PredicateBuilder(String name, CharPredicate predicate) {
		this.name = name;
		this.predicate = predicate;
	}
	
	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		return new CharPredicateExpression(name, predicate);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		return this;
	}

	@Override
	public ExpressionBuilder clone() {
		return new PredicateBuilder(name, predicate);
	}

}
