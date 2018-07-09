package org.bakasoft.gramat.building;

import java.util.ArrayList;
import java.util.List;

abstract public class ExpressionListBuilder extends ExpressionBuilder {

	protected final List<ExpressionBuilder> children;
	
	public ExpressionListBuilder() {
		children = new ArrayList<>();
	}
	
	public List<ExpressionBuilder> getChildren() {
		return children;
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return children.stream()
				.anyMatch(child -> child.hasWildChar(grammarBuilder));
	}
	
	public void addExpressions(List<ExpressionBuilder> list) {
		for (ExpressionBuilder item : list) {
			addExpression(item);
		}
	}

	public void addExpression(ExpressionBuilder child) {
		child.adopt(this);
		
		children.add(child);
	}
}
