package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class RuleBuilder extends StatementBuilder {

	private final String name;
	
	private final ExpressionBuilder expression;

	public RuleBuilder(String name, ExpressionBuilder expression) {
		this.name = name;
		this.expression = expression;
	}
	
	public String getName() {
		return name;
	}

	public ExpressionBuilder getExpression() {
		return expression;
	}

	@Override
	public void build(GrammarBuilder grammarBuilder, Grammar grammar) {
		Expression e = grammarBuilder.build(expression);
		
		grammar.registerRule(name, e);
	}

}
