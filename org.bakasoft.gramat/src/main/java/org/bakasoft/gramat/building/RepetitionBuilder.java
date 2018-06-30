package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;

public class RepetitionBuilder extends ExpressionBuilder {

	private final ExpressionBuilder expression;
	private final Integer minimum;
	private final Integer maximum;
	
	public RepetitionBuilder(ExpressionBuilder expression, Integer minimum, Integer maximum) {
		this.expression = expression;
		this.minimum = minimum;
		this.maximum = maximum;
		
		if (minimum != null && minimum < 0) {
			throw new RuntimeException("not negative");
		}
		
		if (maximum != null && maximum < 0) {
			throw new RuntimeException("not negative");
		}
		
		if (minimum != null && maximum != null && minimum > maximum) {
			throw new RuntimeException("invalid range");
		}
	}
	
	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		Expression e = grammarBuilder.build(expression);
		
		if (minimum == null || minimum == 0) {
			if (maximum == null) {
				return new ZeroOrMore(e);
			}
			else if (maximum == 1) {
				return new ZeroOrOne(e);	
			}
		} else if (minimum == 1) {
			if (maximum == null) {
				return new OneOrMore(e);
			}
			else if (maximum == 1) {
				return e; // 🤦🏽
			}
		}
		
		throw new RuntimeException(); // TODO implement max-min and exact repetitions
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder startExpr = expression.getStartExpression(grammarBuilder);
		
		if (minimum == null || minimum == 0) {
			return new RepetitionBuilder(startExpr, 0, 1);
		}
		
		return startExpr;
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		return null;
	}

}
