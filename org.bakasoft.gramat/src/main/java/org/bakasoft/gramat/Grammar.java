package org.bakasoft.gramat;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.bakasoft.gramat.compiling.Compiler;
import org.bakasoft.gramat.util.CharPredicate;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.errors.IdentifierNotAvailableException;
import org.bakasoft.gramat.errors.RuleNotFoundException;
import org.bakasoft.gramat.regularExpressions.CharPredicateExpression;

public class Grammar {

	private final Engine engine;
	
	private final LinkedHashMap<String, Expression> rules;
	
	public Grammar() {
		this(new Engine());
	}
	
	public Grammar(Engine engine) {
		this.engine = engine;
		this.rules = new LinkedHashMap<>();
	}
	
	public Expression compile(String code) {
		return Compiler.compileExpression(this, new StringTape(code));
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
		registerRule(id, new CharPredicateExpression(this, predicate));
	}

	public Engine getEngine() {
		return engine;
	}
	
}
