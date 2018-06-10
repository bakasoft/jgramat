package org.bakasoft.gramat.propertyExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

public class ObjectProperty extends AbstractProperty {
	
	public ObjectProperty(Grammar grammar, String name, boolean isArray, Expression expr) {
		super(grammar, name, isArray, expr);
	}

	@Override
	public boolean process(Context context) {
		return context.builder.transaction(() -> {
			context.builder.openElement();
			
			if (expr.process(context)) {
				context.builder.closeElement(propertyName, isArray);
				
				return true;
			}
			
			return false;
		});
	}

}
