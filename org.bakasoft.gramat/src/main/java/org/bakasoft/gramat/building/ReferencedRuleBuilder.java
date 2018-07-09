package org.bakasoft.gramat.building;

public class ReferencedRuleBuilder extends ExpressionPlainBuilder {

	private final String name;
	
	public ReferencedRuleBuilder(String name) {
		this.name = name;
	}

	public ExpressionBuilder getReferencedExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = grammarBuilder.getExpression(grammarBuilder, name);
		
		if (expression == null) {
			throw new RuntimeException("rule not defined: '" + name + "'");
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
	public ExpressionBuilder clone(boolean includeProperties) {
		return new ReferencedRuleBuilder(name);
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		ExpressionBuilder expression = getReferencedExpression(grammarBuilder);
		
		return expression.hasWildChar(grammarBuilder);
	}

}
