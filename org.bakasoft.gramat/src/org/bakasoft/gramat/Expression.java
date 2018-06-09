package org.bakasoft.gramat;

import java.util.Map;

import org.bakasoft.gramat.debug.DebugContext;
import org.bakasoft.gramat.debug.DebugObjectBuilder;
import org.bakasoft.gramat.debug.DebugTape;
import org.bakasoft.gramat.debug.Debugger;
import org.bakasoft.gramat.util.DefaultObjectBuilder;
import org.bakasoft.gramat.util.DummyObjectBuilder;
import org.bakasoft.gramat.util.ObjectBuilder;
import org.bakasoft.gramat.util.StringTape;
import org.bakasoft.gramat.util.Tape;

abstract public class Expression {

	abstract public boolean process(Context context);
	
	protected final Grammar grammar;
	
	public Expression(Grammar grammar) {
		this.grammar = grammar;
	}
	
	public Context debug(String input, Debugger debugger) {
		Tape tape = new DebugTape(input, debugger);
		ObjectBuilder builder = new DebugObjectBuilder(debugger);
		Context context = new DebugContext(tape, builder, debugger);
		
		debugger.processStarted(context);
		
		process(context);
		
		debugger.processCompleted(context);
		
		return context;
	}
	
	public boolean test(String input) {
		Tape tape = new StringTape(input);
		ObjectBuilder builder = new DummyObjectBuilder();
		Context context = new Context(tape, builder);
		
		return process(context) && tape.isClosed();
	}
	
	public Map<String, Object> eval(String input) {
		Tape tape = new StringTape(input);
		ObjectBuilder builder = new DefaultObjectBuilder();
		Context context = new Context(tape, builder);
		
		if(process(context) && tape.isClosed()) {
			return builder.build();
		} else {
			throw new RuntimeException(); // add message
		}
	}

	public Grammar getGrammar() {
		return grammar;
	}
	
}
