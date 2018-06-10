package org.bakasoft.gramat;

import java.util.LinkedHashMap;

import org.bakasoft.gramat.compiling.Compiler;
import org.bakasoft.gramat.compiling.importers.DefaultImportResolver;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.errors.GrammarNotFoundException;
import org.bakasoft.gramat.errors.IdentifierNotAvailableException;

public class Engine {

	private final LinkedHashMap<String, Grammar> grammars;
	
	public Engine() {
		grammars = new LinkedHashMap<>();
	}
	
	public void registerDefaultGrammar() {
		registerGrammar("gramat", new DefaultGrammar(this));
	}
	
	public Grammar compile(String code) {
		return Compiler.compileGrammar(this, new StringTape(code), new DefaultImportResolver(this));
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
