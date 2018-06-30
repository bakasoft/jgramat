package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.OneOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrMore;
import org.bakasoft.gramat.regularExpressions.ZeroOrOne;

public class RepetitionBuilder extends ExpressionBuilder {

	private final Integer minimum;
	private final Integer maximum;
	
	public RepetitionBuilder(Integer minimum, Integer maximum) {
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
		ExpressionBuilder content = getSingleChild();
		Expression e = grammarBuilder.build(content);
		
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
		ExpressionBuilder content = getSingleChild();
		ExpressionBuilder startExpr = content.getStartExpression(grammarBuilder);
		
		if (minimum == null || minimum == 0) {
			ExpressionBuilder nextExpr = getNextExpression();
			
			if (nextExpr != null) {
				ExpressionBuilder nextExpr0 = nextExpr.getStartExpression(grammarBuilder);
				DisjunctionSequenceBuilder result = new DisjunctionSequenceBuilder();
				
				result.addExpression(startExpr);
				result.addExpression(nextExpr0);
				
				return result;
			} else {
				RepetitionBuilder result = new RepetitionBuilder(0, 1);
				
				result.addExpression(startExpr);	
			}
		}
		
		return startExpr;
	}

	@Override
	public ExpressionBuilder clone() {
		RepetitionBuilder clone = new RepetitionBuilder(minimum, maximum);
		
		cloneChildrenInto(clone);
		
		return clone;
	}

}
