package org.bakasoft.gramat.building;

import java.util.List;

abstract public class ExpressionItemBuilder extends ExpressionBuilder {

	protected ExpressionBuilder expression;
	
	@Override
	public ExpressionBuilder getNextExpressionTo(ExpressionBuilder item) {
		item.checkParent(this);
		
		if (parent != null) {
			return parent.getNextExpressionTo(this);
		}
		
		return null;
	}

	@Override
	public List<ExpressionBuilder> getChildren() { // TODO is this method needed?
		if (expression != null) {
			return List.of(expression);	
		}
		
		return List.of();
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return expression.hasWildChar(grammarBuilder);
	}
	
	public void setExpression(ExpressionBuilder expression) {
		expression.adopt(this);
		
		this.expression = expression;
	}

	public ExpressionBuilder getExpression() {
		return expression;
	}

}
