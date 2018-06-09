package org.bakasoft.gramat.debug;

import org.bakasoft.gramat.util.StringTape;

public class DebugTape extends StringTape {

	private final Debugger debugger;
	
	public DebugTape(String input, Debugger debugger) {
		super(input);
		this.debugger = debugger;
	}

	private void checkPosition(Runnable action) {
		int prev = getPosition();
		
		action.run();
		
		int curr = getPosition();
		
		if (prev != curr) {
			debugger.tapePositionMoved(this, prev, curr);
		}
	}
	
	@Override
	public char peek() {
		char c = super.peek();
		
		debugger.tapeCharacterPeeked(this, c);
		
		return c;
	}
	
	@Override
	public void consume() {
		checkPosition(() -> {
			super.consume();
		});
	}
	
	@Override
	public void setPosition(int position) {
		checkPosition(() -> {
			super.setPosition(position);
		});
	}
}
