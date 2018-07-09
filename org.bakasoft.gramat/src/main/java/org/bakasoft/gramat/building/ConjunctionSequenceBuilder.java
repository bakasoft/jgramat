package org.bakasoft.gramat.building;

public class ConjunctionSequenceBuilder extends ExpressionListBuilder {

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (getChildren().isEmpty()) {
			throw new RuntimeException(); // TODO empty list
		}
		
		return getChildren().get(0).getStartExpression(grammarBuilder);
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		ConjunctionSequenceBuilder clone = new ConjunctionSequenceBuilder();
		
		for (ExpressionBuilder child : children) {
			clone.addExpression(child.clone(includeProperties));
		}
		
		return clone;
	}

	@Override
	public ExpressionBuilder getNextExpressionTo(ExpressionBuilder item) {
		for (int i = 0; i < children.size(); i++) {
			if (children.get(i) == item) {
				if (i + 1 < children.size()) {
					return children.get(i + 1); 	
				} else if (parent != null) {
					return parent.getNextExpression();
				}
			}
		}

		return null;
	}

}
