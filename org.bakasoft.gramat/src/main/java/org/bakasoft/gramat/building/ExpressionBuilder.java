package org.bakasoft.gramat.building;

import java.util.ArrayList;
import java.util.List;

import org.bakasoft.gramat.Expression;

abstract public class ExpressionBuilder {

	// TODO add a mark for immutable expressions and optimize them

	abstract protected Expression generateExpression(GrammarBuilder grammarBuilder);
	
	abstract public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder);

	@Override
	abstract public ExpressionBuilder clone();
	
	private final List<ExpressionBuilder> children;
	
	private ExpressionBuilder parent;
	
	public ExpressionBuilder() {
		children = new ArrayList<>();
	}

	public final ExpressionBuilder getSingleChild() {
		if (children.size() != 1) {
			throw new RuntimeException("too much children");
		}
		
		return children.get(0);
	}
	
	public final List<ExpressionBuilder> getChildren() {
		return children;
	}
	
	public void addExpressions(List<ExpressionBuilder> list) {
		for (ExpressionBuilder item : list) {
			addExpression(item);
		}
	}

	public final void addExpression(ExpressionBuilder child) {
		if (child.parent != null) {
			throw new RuntimeException("the kid is not my son");
		}
		
		child.parent = this;
		
		children.add(child);
	}
	
	public final ExpressionBuilder getNextExpression() {
		if (parent != null) {
			List<ExpressionBuilder> siblings = parent.getChildren();
			
			for (int i = 0; i < siblings.size(); i++) {
				if (siblings.get(i) == this) {
					if (i + 1 < siblings.size()) {
						return siblings.get(i + 1); 	
					} else {
						return parent.getNextExpression();
					}
				}
			}
		}
		
		return null;
	}
	
	public ExpressionBuilder getParent() {
		return parent;
	}
	
	public ExpressionBuilder getComplement() {
		if (this instanceof ComplementBuilder) {
			return ((ComplementBuilder)this).getSingleChild();
		}
		
		ComplementBuilder complement = new ComplementBuilder();
		ExpressionBuilder clone = this.clone();
		
		complement.addExpression(clone);
		
		return complement;
	}
	
	protected void cloneChildrenInto(ExpressionBuilder exprClone) {
		for (ExpressionBuilder child : children) {
			ExpressionBuilder childClone = child.clone();
			
			exprClone.addExpression(childClone);
		}
	}

}
