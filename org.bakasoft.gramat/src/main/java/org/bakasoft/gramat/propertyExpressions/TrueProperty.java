package org.bakasoft.gramat.propertyExpressions;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class TrueProperty extends AbstractProperty {
	
	public TrueProperty(String name, boolean isArray, Expression expr) {
		super(name, isArray, expr);
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

	@Override
	public void toString(GramatWriter writer) {
		writer.writeName(propertyName);
		writer.write(':');
		writer.write('?');
		if (isArray) {
			writer.write('+');	
		}
		writer.write('<');
		writer.write(expr);
		writer.write('>');
	}

}
