package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;

abstract public class ExpressionBuilder {

	// TODO add a mark for immutable expressions and optimize them
	
	abstract protected Expression generateExpression(GrammarBuilder context);
	
	abstract public ExpressionBuilder getStartExpression(GrammarBuilder context);

	abstract public ExpressionBuilder getNextExpression(ExpressionBuilder child);

	public ExpressionBuilder getComplement() {
		if (this instanceof ComplementBuilder) {
			return ((ComplementBuilder)this).getExpression();
		}
		
		return new ComplementBuilder(this);	
	}

}
