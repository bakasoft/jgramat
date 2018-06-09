package org.bakasoft.gramat.regularExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class ReferencedExpression extends Expression {

	private final String id;
	
	private Expression expression;
	
	public ReferencedExpression(Grammar grammar, String id) {
		super(grammar);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean process(Context context) {
		// TODO add mechanism to check if the buffered expression is the same than the one in the grammar (maybe using versions) 
		
		if (expression == null) {
			expression = grammar.findRule(id);	
		}
		
		if (expression == null) {
			throw new RuntimeException("missing rule: " + id);
		}
		
		return context.process(expression);
	}

}
