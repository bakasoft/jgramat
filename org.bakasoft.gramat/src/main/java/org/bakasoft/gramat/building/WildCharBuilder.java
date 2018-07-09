package org.bakasoft.gramat.building;

public class WildCharBuilder extends ExpressionPlainBuilder {

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder nextExpr = getNextExpression();
		
		if (nextExpr != null) {
			return nextExpr.getStartExpression(grammarBuilder);
		}
		
		return new AnyBuilder();
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		return new WildCharBuilder();
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return true;
	}

}
