package org.bakasoft.gramat.propertyExpressions;

import java.math.BigDecimal;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.io.GramatWriter;

public class NumberProperty extends AbstractProperty {
	
	public NumberProperty(String name, boolean isArray, Expression expr) {
		super(name, isArray, expr);
	}

	@Override
	public boolean process(Context context) {
		return context.builder.transaction(() -> {
			int position = context.tape.getPosition();
			
			if (expr.process(context)) {
				String value = context.tape.substring(position, context.tape.getPosition());
				
				// TODO enhance the number parser
				context.builder.setAttribute(propertyName, new BigDecimal(value), isArray);
				
				return true;
			}
			
			return false;
		});
	}

	@Override
	public void toString(GramatWriter writer) {
		writer.writeName(propertyName);
		writer.write(':');
		writer.write('#');
		if (isArray) {
			writer.write('+');	
		}
		writer.write('<');
		writer.write(expr);
		writer.write('>');
	}
	
}
