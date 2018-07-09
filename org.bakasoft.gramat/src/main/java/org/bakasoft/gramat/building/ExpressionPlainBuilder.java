package org.bakasoft.gramat.building;

import java.util.List;

abstract public class ExpressionPlainBuilder extends ExpressionBuilder {

	@Override
	public ExpressionBuilder getNextExpressionTo(ExpressionBuilder item) {
		item.checkParent(this);
		
		if (parent != null) {
			return parent.getNextExpressionTo(this);
		}
		
		return null;
	}

	@Override
	public List<ExpressionBuilder> getChildren() {
		return List.of();
	}

}
