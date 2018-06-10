package org.bakasoft.gramat.debug;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.util.ObjectBuilder;
import org.bakasoft.gramat.util.Tape;

public class DebugContext extends Context {

	private final Debugger debugger;
	
	public DebugContext(Tape tape, ObjectBuilder builder, Debugger debugger) {
		super(tape, builder);
		this.debugger = debugger;
	}
	
	@Override
	public boolean process(Expression expression) {
		boolean result;
		
		debugger.expressionEntered(expression);
		
		result = super.process(expression);
		
		debugger.expressionExited(expression, result);
		
		return result;
	}

}
