package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;

public class WildCharBuilder extends ExpressionBuilder {

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder startExpr = getStartExpression(grammarBuilder);	
		
		return grammarBuilder.build(startExpr.getComplement());
	}
	
	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder nextExpr = getNextExpression();
		
		if (nextExpr != null) {
			return nextExpr.getStartExpression(grammarBuilder);
		}
		
		return new AnyBuilder();
	}

	@Override
	public ExpressionBuilder clone() {
		return new WildCharBuilder();
	}

}
