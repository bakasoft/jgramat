package org.bakasoft.gramat;

import org.bakasoft.gramat.util.ObjectBuilder;
import org.bakasoft.gramat.util.Tape;

public class Context {

	public final Tape tape;
	public final ObjectBuilder builder;
	
	public Context(Tape tape, ObjectBuilder builder) {
		this.tape = tape;
		this.builder = builder;
	}
	
	public boolean process(Expression expression) {
		return expression.process(this);
	}
	
}
