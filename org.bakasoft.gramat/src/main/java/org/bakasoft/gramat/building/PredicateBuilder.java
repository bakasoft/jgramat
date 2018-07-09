package org.bakasoft.gramat.building;

import org.bakasoft.gramat.util.CharPredicate;

public class PredicateBuilder extends ExpressionPlainBuilder {

	private final String name;
	private final CharPredicate predicate;
	
	public PredicateBuilder(String name, CharPredicate predicate) {
		this.name = name;
		this.predicate = predicate;
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		return this;
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		return new PredicateBuilder(name, predicate);
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public CharPredicate getPredicate() {
		return predicate;
	}

}
