package org.bakasoft.gramat;

import java.util.LinkedHashMap;

import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;
import org.bakasoft.gramat.building.GrammarBuilder;
import org.bakasoft.gramat.errors.GrammarNotFoundException;
import org.bakasoft.gramat.errors.IdentifierNotAvailableException;
import org.bakasoft.gramat.io.Parser;

public class Engine {

	private final LinkedHashMap<String, Grammar> grammars;
	
	public Engine() {
		grammars = new LinkedHashMap<>();
	}
	
	public Grammar compile(String code) {
		Tape tape = new StringTape(code);
		GrammarBuilder builder = Parser.parseGrammar(tape);
		
		return builder.build();
	}
	
	public void registerGrammar(String id, Grammar grammar) {
		synchronized(grammars) {
			if (grammars.containsKey(id)) {
				throw new IdentifierNotAvailableException(id);
			}
			
			grammars.put(id, grammar);	
		}
	}
	
	public Grammar findGrammar(String id) {
		Grammar grammar;
		
		synchronized(grammars) {
			grammar = grammars.get(id);
		}
		
		if (grammar == null) {
			throw new GrammarNotFoundException(id);	
		}
		
		return grammar;
	}
	
}
