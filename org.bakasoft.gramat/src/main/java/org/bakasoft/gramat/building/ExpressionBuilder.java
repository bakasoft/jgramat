package org.bakasoft.gramat.building;

import java.util.List;

abstract public class ExpressionBuilder {

	// TODO add a mark for immutable expressions and optimize them

	abstract public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder);
	
	abstract public ExpressionBuilder getNextExpressionTo(ExpressionBuilder item);

	abstract public List<ExpressionBuilder> getChildren();
	
	abstract public boolean hasWildChar(GrammarBuilder grammarBuilder);
	
	abstract public ExpressionBuilder clone(boolean includeProperties);
	
	protected ExpressionBuilder parent;
	
	public final ExpressionBuilder getNextExpression() {		
		if (parent != null) {
			return parent.getNextExpressionTo(this);
		}
		
		return null;
	}
	
	public ExpressionBuilder getParent() {
		return parent;
	}
	
	public ExpressionBuilder getComplement() {
		if (this instanceof ComplementBuilder) {
			return ((ComplementBuilder)this).getExpression().clone(false);
		}
		
		ComplementBuilder complement = new ComplementBuilder();
		ExpressionBuilder clone = this.clone(false);
		
		complement.setExpression(clone);
		
		return complement;
	}
	
	protected void checkParent(ExpressionBuilder parent) {
		if (this.parent != parent) {
			throw new RuntimeException("the kid is not my son");
		}
	}
	
	protected void adopt(ExpressionBuilder parent) {
		if (this.parent != null) {
			throw new RuntimeException("the kid is not my son");
		}
		
		this.parent = parent;
	}

}
