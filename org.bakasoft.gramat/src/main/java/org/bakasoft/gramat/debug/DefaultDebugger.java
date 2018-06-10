package org.bakasoft.gramat.debug;

import java.io.PrintStream;

import org.bakasoft.gramat.Context;
import org.bakasoft.gramat.Expression;
import org.bakasoft.gramat.util.Tape;

public class DefaultDebugger implements Debugger {
	
	private final PrintStream out;
	
	private int indent;
	
	private int sampleLength;
	
	public DefaultDebugger(PrintStream out) {
		this.out = out;
		this.sampleLength = 50;
	}
	
	public void reset() {
		indent = 0;
	}
	
	private void log(String message) {
		if (out != null) {
			String[] lines = message.trim().split("\r?\n");
			
			for (String line : lines) {
				for (int i = 0; i < indent; i++) {
					out.print("  ");	
				}
				
				out.println(line);	
			}	
		}
	}

	@Override
	public void processStarted(Context context) {
		log("Process started...");
	}

	@Override
	public void tapeCharacterPeeked(Tape tape, char c) {
		log("Peek: " + Debugger.inspect(tape, sampleLength));
	}
	
	@Override
	public void tapePositionMoved(Tape tape, int previous, int current) {
		log("Move: " + Debugger.inspect(tape, sampleLength));
	}

	@Override
	public void processCompleted(Context context) {
		log("Process Completed.");
	}

	@Override
	public void expressionEntered(Expression expression) {
		log("Enter: " + Debugger.inspect(expression, sampleLength));
		
		indent++;
	}

	@Override
	public void expressionExited(Expression expression, boolean result) {
		indent--;
		
		log("Exit: " + (result ? "passed" : "failed"));
	}

	@Override
	public void objectAttributeSet(String name, Object value, boolean array) {
		if (array) {
			log("Set value: " + name + ": " + value);
		} else {
			log("Add value: " + name + ": " + value);
		}
	}

	@Override
	public void objectElementSet(String name, boolean array) {
		if (array) {
			log("Set element: " + name);
		} else {
			log("Add element: " + name);
		}
	}

	@Override
	public void objectTransactionBegun() {
		log("Begin transaction");	
	}

	@Override
	public void objectTransactionCompleted(boolean result) {
		if (result) {
			log("Commit transaction");
		} else {
			log("Rollback transaction");
		}
	}
	
}
