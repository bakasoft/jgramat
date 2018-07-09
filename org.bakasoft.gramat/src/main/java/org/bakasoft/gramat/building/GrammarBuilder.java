package org.bakasoft.gramat.building;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Setting;
import org.bakasoft.gramat.building.importers.ImportResolver;

public class GrammarBuilder {

	private final ArrayList<StatementBuilder> statements;
	private final HashMap<Setting, Object> settings;
	private final HashMap<String, GrammarBuilder> imports;
	
	private ImportResolver importResolver;
	
	public GrammarBuilder() {
		this.statements = new ArrayList<>();
		this.settings = new HashMap<>();
		this.imports = new HashMap<>();
	}
	
	public void addRule(String name, ExpressionBuilder expression) {
		addStatement(new RuleBuilder(name, expression));
	}

	public void addStatement(StatementBuilder statement) {
		// TODO check duplicated names
		statements.add(statement);
	}
	
	public Grammar build() {
		Grammar grammar = new Grammar();
		
		build(grammar);
		
		return grammar;
	}
	
	public void build(Grammar grammar) {
		for (StatementBuilder statement : statements) {
			statement.build(this, grammar);
		}
	}
	
	public boolean getBoolean(Setting setting) {
		Object value = settings.get(setting);
		
		if (value == null) {
			return false;
		} else if (value instanceof Boolean) {
			return ((Boolean)value);
		} else {
			throw new RuntimeException(); // TODO invalid configuration value
		}
	}
	
	public void setImport(String path, GrammarBuilder grammarBuilder) {
		imports.put(path, grammarBuilder);
	}
	
	public GrammarBuilder getImport(String path) {
		return imports.computeIfAbsent(path, key -> {
			GrammarBuilder grammarBuilder = null;
						
			if (importResolver != null) {
				grammarBuilder = importResolver.getGrammar(key);	
			}
			
			if (grammarBuilder == null) {
				// built-in rules
				if ("gramat".equals(key)) {
					grammarBuilder = new GrammarBuilder();
					grammarBuilder.addRule("any", new PredicateBuilder("any", c -> true));
					grammarBuilder.addRule("whitespace", new PredicateBuilder("whitespace", Character::isWhitespace));
				} else {
					throw new RuntimeException("path not found: " + key);	
				}
			}
			
			return grammarBuilder;
		});
	}

	public ImportResolver getImportResolver() {
		return importResolver;
	}

	public void setImportResolver(ImportResolver importResolver) {
		this.importResolver = importResolver;
	}

	
	public List<StatementBuilder> getStatements() {
		return statements;
	}
	
	// TODO rename to getExpressionBuilder?
	public ExpressionBuilder getExpression(GrammarBuilder grammarBuilder, String name) { 
		ArrayList<ExpressionBuilder> result = new ArrayList<>();
		
		for (StatementBuilder statement : statements) {
			if (statement instanceof ImportBuilder) {
				ImportBuilder importBuilder = (ImportBuilder)statement;
				
				if (importBuilder.containsName(name)) {
					result.add(importBuilder.getExpressionBuilder(grammarBuilder, name));
				}
			}
			else if (statement instanceof RuleBuilder) {
				RuleBuilder rule = (RuleBuilder)statement;
				
				if (Objects.equals(rule.getName(), name)) {
					result.add(rule.getExpression());
				}
			}
			
			if (result.size() > 1) {
				throw new RuntimeException("Ambiguous rule: " + name);
			}
		}
		
		return result.isEmpty() ? null : result.get(0);
	}	
}
