package org.bakasoft.gramat.building;

public class PropertyBuilder extends ExpressionItemBuilder {

	private final String name;
	private final PropertyType type;
	private final boolean array;
	
	public PropertyBuilder(String name, PropertyType type, boolean array) {
		this.name = name;
		this.type = type;
		this.array = array;
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		if (includeProperties) {
			PropertyBuilder clone = new PropertyBuilder(name, type, array);
			
			clone.setExpression(expression.clone(includeProperties));
			
			return clone;
		}
		
		return expression.clone(includeProperties);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		ExpressionBuilder content = getExpression();
		
		return content.getStartExpression(grammarBuilder);
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
