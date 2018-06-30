package org.bakasoft.gramat.building;

import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.regularExpressions.FuzzyString;
import org.bakasoft.gramat.regularExpressions.StrictString;

public class StringLiteralBuilder extends ExpressionBuilder {
	
	private final String text;
	
	private final boolean fuzzy;

	public StringLiteralBuilder(String text, boolean fuzzy) {
		this.text = text;
		this.fuzzy = fuzzy;
	}
	
	@Override
	protected Expression generateExpression(GrammarBuilder grammarBuilder) {
		if (fuzzy) {
			return new FuzzyString(text);
		}

		// TODO optimize immutable expressions by returning the same instance instead of a new one
		return new StrictString(text);
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (text.isEmpty()) {
			throw new RuntimeException(); // TODO empty literal
		}
		
		char firstChar = text.charAt(0);
		
		return new StringLiteralBuilder(String.valueOf(firstChar), fuzzy);
	}

	@Override
	public ExpressionBuilder getNextExpression(ExpressionBuilder child) {
		return null;
	}

	public String getText() {
		return text;
	}

	public boolean isFuzzy() {
		return fuzzy;
	}
}
