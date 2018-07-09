package org.bakasoft.gramat.building;

public class RepetitionBuilder extends ExpressionItemBuilder {

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
	
	public Integer getMinimum() {
		return minimum;
	}
	
	public Integer getMaximum() {
		return maximum;
	}
	
	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = getExpression();
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
				
				result.setExpression(startExpr);	
			}
		}
		
		return startExpr;
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		RepetitionBuilder clone = new RepetitionBuilder(minimum, maximum);
		
		clone.setExpression(expression.clone(includeProperties));
		
		return clone;
	}

}
