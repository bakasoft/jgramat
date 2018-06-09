package org.bakasoft.gramat;

public class DefaultGrammar extends Grammar {

	public DefaultGrammar(Engine engine) {
		super(engine);
		
		this.registerRule("any", (c -> true));
		this.registerRule("whitespace", Character::isWhitespace);
	}
	
}
