package org.bakasoft.gramat.debug;

import java.util.function.BooleanSupplier;

import org.bakasoft.gramat.util.DefaultObjectBuilder;

public class DebugObjectBuilder extends DefaultObjectBuilder {

	private final Debugger debugger;

	public DebugObjectBuilder(Debugger debugger) {
		this.debugger = debugger;
	}
	
	@Override
	public void setAttribute(String name, Object value, boolean array) {
		super.setAttribute(name, value, array);
		
		debugger.objectAttributeSet(name, value, array);
	}
	
	@Override
	public void closeElement(String name, boolean array) {
		super.closeElement(name, array);
		
		debugger.objectElementSet(name, array);
	}
	
	@Override
	public boolean transaction(BooleanSupplier action) {
		debugger.objectTransactionBegun();
		
		boolean result = super.transaction(action);
		
		debugger.objectTransactionCompleted(result);
		
		return result;
	}
}

