package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.Complement;

public class ComplementBuilder extends ExpressionBuilder {

	private final ExpressionBuilder expression;
	
	public ComplementBuilder(ExpressionBuilder expression) {
		this.expression = expression;
	}

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		return new Complement(grammarBuilder.build(expression));
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder startExpr = expression.getStartExpression(grammarBuilder);
		
		return startExpr.getComplement();
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		return null;
	}
	
	public ExpressionBuilder getExpression() {
		return expression;
	}
	
}
