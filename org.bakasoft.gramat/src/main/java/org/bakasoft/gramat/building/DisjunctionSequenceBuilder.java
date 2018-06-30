package org.bakasoft.gramat.building;

import java.util.List;
import java.util.stream.Collectors;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.DisjunctionSequence;

public class DisjunctionSequenceBuilder extends ExpressionBuilder {

	private final List<ExpressionBuilder> items;
	
	public DisjunctionSequenceBuilder(List<ExpressionBuilder> items) {
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
		
		return new DisjunctionSequence(expressions);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (items.isEmpty()) {
			throw new RuntimeException(); // TODO empty list
		}
		
		return new DisjunctionSequenceBuilder(items.stream()
				.map(item -> item.getStartExpression(grammarBuilder))
				.collect(Collectors.toList()));
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		return null;
	}
	
	public List<ExpressionBuilder> getExpressions() {
		return items;
	}

}
