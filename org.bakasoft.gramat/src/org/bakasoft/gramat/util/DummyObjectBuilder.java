package org.bakasoft.gramat.util;

import java.util.Map;
import java.util.function.BooleanSupplier;

public class DummyObjectBuilder implements ObjectBuilder {

	@Override
	public boolean transaction(BooleanSupplier action) {
		return action.getAsBoolean();
	}

	@Override
	public void openElement() {
		// do nothing
	}

	@Override
	public void setAttribute(String name, Object value, boolean array) {
		// do nothing
	}

	@Override
	public void closeElement(String name, boolean array) {
		// do nothing
	}

	@Override
	public Map<String, Object> build() {
		throw new UnsupportedOperationException(); 
	}

}
