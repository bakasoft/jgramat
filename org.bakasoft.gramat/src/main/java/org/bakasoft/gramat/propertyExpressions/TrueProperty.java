package org.bakasoft.gramat.propertyExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class TrueProperty extends AbstractProperty {
	
	public TrueProperty(Grammar grammar, String name, boolean isArray, Expression expr) {
		super(grammar, name, isArray, expr);
	}

	@Override
	public boolean process(Context context) {
		return context.builder.transaction(() -> {
			if (expr.process(context)) {
				context.builder.setAttribute(propertyName, true, isArray);
				
				return true;
			}
			
			return false;
		});
	}

}
