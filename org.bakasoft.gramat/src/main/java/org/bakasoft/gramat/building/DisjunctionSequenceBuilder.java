package org.bakasoft.gramat.building;

public class DisjunctionSequenceBuilder extends ExpressionListBuilder {

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (getChildren().isEmpty()) {
			throw new RuntimeException(); // TODO empty list
		}
		
		DisjunctionSequenceBuilder startExpr = new DisjunctionSequenceBuilder();
		
		for (ExpressionBuilder child : getChildren()) {
			ExpressionBuilder childStart = child.getStartExpression(grammarBuilder);
			
			startExpr.addExpression(childStart);
		}
		
		return startExpr;
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		DisjunctionSequenceBuilder clone = new DisjunctionSequenceBuilder();
		
		for (ExpressionBuilder child : children) {
			clone.addExpression(child.clone(includeProperties));
		}
		
		return clone;
	}

	@Override
	public ExpressionBuilder getNextExpressionTo(ExpressionBuilder item) {
		return null;
	}
}
