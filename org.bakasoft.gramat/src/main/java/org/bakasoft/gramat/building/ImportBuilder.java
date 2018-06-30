package org.bakasoft.gramat.building;

import java.util.Map;

import org.bakasoft.gramat.Grammar;

public class ImportBuilder extends StatementBuilder {

	private final String externalPath;
	private final Map<String, String> nameMapping;
	
	public ImportBuilder(String externalPath, Map<String, String> nameMapping) {
		this.externalPath = externalPath;
		this.nameMapping = nameMapping;
	}
	
	public boolean containsName(String name) {
		return nameMapping.containsKey(name);
	}
	
	public String getExternalPath() {
		return externalPath;
	}

	public ExpressionBuilder getExpressionBuilder(GrammarBuilder grammarBuilder, String localName) {
		String externalName = nameMapping.get(localName);
		
		if (externalName == null) {
			throw new RuntimeException("unknown name: " + localName);
		}
		
		GrammarBuilder externalGrammar = grammarBuilder.getImport(externalPath);
		
		if (externalGrammar == null) {
			throw new RuntimeException("Grammar not found: " + externalPath);
		}
		
		ExpressionBuilder expressionBuilder = externalGrammar.getExpression(grammarBuilder, externalName);
		
		if (expressionBuilder == null) {
			throw new RuntimeException("Rule not found: " + externalName + " in grammar: " + externalPath);
		}
		
		return expressionBuilder;
	}

	@Override
	public void build(GrammarBuilder grammarBuilder, Grammar grammar) {
		// do nothing
	}

}
