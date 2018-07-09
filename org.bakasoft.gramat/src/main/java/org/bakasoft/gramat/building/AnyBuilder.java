package org.bakasoft.gramat.building;

public class AnyBuilder extends ExpressionPlainBuilder {

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		return this;
	}

	@Override
	public AnyBuilder clone(boolean includeProperties) {
		return new AnyBuilder();
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return true;
	}
}
