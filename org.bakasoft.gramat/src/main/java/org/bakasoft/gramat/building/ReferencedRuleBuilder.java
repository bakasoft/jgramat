package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;

public class ReferencedRuleBuilder extends ExpressionBuilder {

	private final String name;
	
	public ReferencedRuleBuilder(String name) {
		this.name = name;
	}
	
	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = getReferencedExpression(grammarBuilder);
		
		return grammarBuilder.build(expression);
	}

	public ExpressionBuilder getReferencedExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = grammarBuilder.getExpression(grammarBuilder, name);
		
		if (expression == null) {
			throw new RuntimeException("rule not defined");
		}
		
		return expression;
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = getReferencedExpression(grammarBuilder);
		
		return expression.getStartExpression(grammarBuilder);
	}
	
	public String getName() {
		return name;
	}

	@Override
	public ExpressionBuilder clone() {
		return new ReferencedRuleBuilder(name);
	}

}
