package org.bakasoft.gramat.util;

import java.util.Map;
import java.util.function.BooleanSupplier;

public interface ObjectBuilder {
	
	boolean transaction(BooleanSupplier action);

	void openElement();
	
	void setAttribute(String name, Object value, boolean array);
	
	void closeElement(String name, boolean array);
	
	Map<String, Object> build();
}
