package org.bakasoft.gramat.propertyExpressions;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.Grammar;

abstract public class AbstractProperty extends Expression {
	
	protected final String propertyName;
	protected final boolean isArray;
	protected final Expression expr;
	
	public AbstractProperty(Grammar grammar, String name, boolean isArray, Expression expr) {
		super(grammar);
		this.propertyName = name;
		this.isArray = isArray;
		this.expr = expr;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public boolean isArray() {
		return isArray;
	}

	public Expression getValueExpression() {
		return expr;
	}
	
	
}