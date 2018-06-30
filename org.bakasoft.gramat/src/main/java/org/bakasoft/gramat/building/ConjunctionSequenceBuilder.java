package org.bakasoft.gramat.building;

import java.util.List;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.ConjunctionSequence;

public class ConjunctionSequenceBuilder extends ExpressionBuilder {

	private final List<ExpressionBuilder> items;

	public ConjunctionSequenceBuilder(List<ExpressionBuilder> items) {
		this.items = items;
	}
	
	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		Expression[] expressions = items.stream()
				.map(item -> grammarBuilder.build(item))
				.toArray(Expression[]::new);
		
		if (expressions.length == 0) {
			throw new RuntimeException(); // TODO empty expression error
		} else if (expressions.length == 1) {
			return expressions[0];
		}
		
		return new ConjunctionSequence(expressions);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (items.isEmpty()) {
			throw new RuntimeException(); // TODO empty list
		}
		
		return items.get(0).getStartExpression(grammarBuilder);
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		int index = items.indexOf(child);
		
		if (index == -1) {
			throw new RuntimeException("the kid is not my son");
		}
		
		index++;
		
		if (index < items.size()) {
			return items.get(index);
		}
		
		return null;
	}
	
	public List<ExpressionBuilder> getExpressions() {
		return items;
	}

}
