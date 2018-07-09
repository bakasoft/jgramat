package org.bakasoft.gramat.building;

public class ComplementBuilder extends ExpressionItemBuilder {

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = getExpression();
		ExpressionBuilder startExpr = content.getStartExpression(grammarBuilder);
		
		return startExpr.getComplement();
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		ComplementBuilder clone = new ComplementBuilder();
		
		clone.setExpression(expression.clone(includeProperties));
		
		return clone;
	}

}
