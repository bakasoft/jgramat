package org.bakasoft.gramat.building;

import java.util.ArrayList;
import java.util.List;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;

public class DisjunctionSequenceBuilder extends ExpressionBuilder {

	private void collapseExpressions(GrammarBuilder grammarBuilder, List<Expression> expressions) {
		for (ExpressionBuilder child : getChildren()) {
			if (child instanceof DisjunctionSequenceBuilder) {
				DisjunctionSequenceBuilder subseq = (DisjunctionSequenceBuilder)child;
				
				subseq.collapseExpressions(grammarBuilder, expressions);
			} else {
				Expression expr = grammarBuilder.build(child);
				
				if (!expressions.contains(expr)) {
					expressions.add(expr);		
				}
			}
		}
	}

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		ArrayList<Expression> expressions = new ArrayList<>();
		
		collapseExpressions(grammarBuilder, expressions);
		
		if (expressions.isEmpty()) {
			throw new RuntimeException(); // TODO empty expression error
		} else if (expressions.size() == 1) {
			return expressions.get(0);
		}
		
		return new DisjunctionSequence(expressions.toArray(new Expression[expressions.size()]));
	}

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
	public ExpressionBuilder clone() {
		DisjunctionSequenceBuilder clone = new DisjunctionSequenceBuilder();
		
		cloneChildrenInto(clone);
		
		return clone;
	}
}
