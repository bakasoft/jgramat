package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.Complement;

public class ComplementBuilder extends ExpressionBuilder {

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = getSingleChild();
		Expression e = grammarBuilder.build(content);
		
		return new Complement(e);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = getSingleChild();
		ExpressionBuilder startExpr = content.getStartExpression(grammarBuilder);
		
		return startExpr.getComplement();
	}

	@Override
	public ExpressionBuilder clone() {
		ComplementBuilder clone = new ComplementBuilder();
		
		cloneChildrenInto(clone);
		
		return clone;
	}

}
