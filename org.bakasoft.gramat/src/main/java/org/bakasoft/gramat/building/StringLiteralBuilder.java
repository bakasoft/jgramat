package org.bakasoft.gramat.building;

public class StringLiteralBuilder extends ExpressionPlainBuilder {
	
	private final String text;
	
	private final boolean fuzzy;

	public StringLiteralBuilder(String text, boolean fuzzy) {
		this.text = text;
		this.fuzzy = fuzzy;
	}

	@Override
	public ExpressionBuilder getStartExpression(GrammarBuilder grammarBuilder) {
		if (text.isEmpty()) {
			throw new RuntimeException(); // TODO empty literal
		}
		
		char firstChar = text.charAt(0);
		
		return new StringLiteralBuilder(String.valueOf(firstChar), fuzzy);
	}

	public String getText() {
		return text;
	}

	public boolean isFuzzy() {
		return fuzzy;
	}

	@Override
	public ExpressionBuilder clone(boolean includeProperties) {
		return new StringLiteralBuilder(text, fuzzy);
	}

	@Override
	public boolean hasWildChar(GrammarBuilder grammarBuilder) {
		return false;
	}
}
