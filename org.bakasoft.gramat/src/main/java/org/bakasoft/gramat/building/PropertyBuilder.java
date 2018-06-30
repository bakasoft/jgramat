package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.propertyExpressions.FalseProperty;
import org.bakasoft.gramat.propertyExpressions.NullProperty;
import org.bakasoft.gramat.propertyExpressions.NumberProperty;
import org.bakasoft.gramat.propertyExpressions.ObjectProperty;
import org.bakasoft.gramat.propertyExpressions.StringProperty;
import org.bakasoft.gramat.propertyExpressions.TrueProperty;

public class PropertyBuilder extends ExpressionBuilder {

	private final String name;
	private final ExpressionBuilder expression;
	private final PropertyType type;
	private final boolean array;
	
	public PropertyBuilder(String name, ExpressionBuilder expression, PropertyType type, boolean array) {
		this.name = name;
		this.expression = expression;
		this.type = type;
		this.array = array;
	}

	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		Expression e = grammarBuilder.build(expression);
		
		switch(type) {
		case FALSE: return new FalseProperty(name, array, e);
		case NULL: return new NullProperty(name, array, e);
		case NUMBER: return new NumberProperty(name, array, e);
		case OBJECT: return new ObjectProperty(name, array, e);
		case STRING: return new StringProperty(name, array, e);
		case TRUE: return new TrueProperty(name, array, e);
		default: 
			throw new RuntimeException();
		}
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		return expression.getStartExpression(grammarBuilder);
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		return null;
	}

	public ExpressionBuilder getExpression() {
		return expression;
	}
	
	public String getName() {
		return name;
	}

	public PropertyType getType() {
		return type;
	}

	public boolean isArray() {
		return array;
	}

}
