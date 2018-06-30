package org.bakasoft.gramat;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.bakasoft.gramat.util.CharPredicate;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;
import org.bakasoft.gramat.building.ExpressionBuilder;
import org.bakasoft.gramat.building.GrammarBuilder;
import org.bakasoft.gramat.errors.IdentifierNotAvailableException;
import org.bakasoft.gramat.errors.RuleNotFoundException;
import org.bakasoft.gramat.io.Parser;
import org.bakasoft.gramat.regularExpressions.CharPredicateExpression;

public class Grammar {
	
	private final GrammarBuilder builder;
	private final LinkedHashMap<String, Expression> rules;
	
	public Grammar() {
		this(new GrammarBuilder());
	}
	
	public Grammar(GrammarBuilder builder) {
		this.builder = builder;
		this.rules = new LinkedHashMap<>();
	}
	
	public Grammar(String code) {
		this(Parser.parseGrammar(new StringTape(code)));
		
		builder.build(this);
	}
	
	public Expression compile(String code) {
		Tape tape = new StringTape(code);
		ExpressionBuilder expressionBuilder = Parser.parseExpression(tape);
		
		return builder.build(expressionBuilder);
	}
	
	public String getId(Expression rule) {
		Set<String> ids = rules.entrySet().stream()
				.filter(entry -> entry.getValue() == rule)
				.map(entry -> entry.getKey())
				.collect(Collectors.toSet());
		
		if (ids.size() == 1) {
			return ids.stream().findFirst().get();
		}
			
		return null;
	}
	
	public Expression getRule(String name) {
		return rules.get(name);
	}
	
	public Expression findRule(String id) {
		if (!rules.containsKey(id)) {
			throw new RuleNotFoundException(id);
		}
		
		return rules.get(id);
	}
	
	public void registerRule(String id, Expression expr) {
		if (rules.get(id) != null || (rules.containsKey(id) && expr == null)) {
			throw new IdentifierNotAvailableException(id);
		}
		
		rules.put(id, expr);
	}
	
	public void registerRule(String id) {
		registerRule(id, (Expression)null);
	}
	
	public void registerRule(String id, CharPredicate predicate) {
		registerRule(id, new CharPredicateExpression(id, predicate));
	}
}
