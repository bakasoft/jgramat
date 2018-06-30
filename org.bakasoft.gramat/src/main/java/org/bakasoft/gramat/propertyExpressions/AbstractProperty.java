package org.bakasoft.gramat.propertyExpressions;

import java.util.Objects;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.util.CompareTool;

abstract public class AbstractProperty extends Expression {
	
	protected final String propertyName;
	protected final boolean isArray;
	protected final Expression expr;
	
	public AbstractProperty(String name, boolean isArray, Expression expr) {
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

	@Override
	public int hashCode() {
		return CompareTool.hashMix(propertyName, isArray, expr, getClass());
	}

	@Override
	public boolean equals(Object obj) {
		AbstractProperty other = CompareTool.checkType(obj, AbstractProperty.class);
		
		return other != null
				&& Objects.equals(this.getClass(), other.getClass())
				&& Objects.equals(this.propertyName, other.propertyName)
				&& Objects.equals(this.isArray, other.isArray)
				&& Objects.equals(this.expr, other.expr);
	}
	
}
