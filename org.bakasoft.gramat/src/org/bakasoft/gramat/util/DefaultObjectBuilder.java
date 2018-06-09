package org.bakasoft.gramat.util;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BooleanSupplier;

public class DefaultObjectBuilder implements ObjectBuilder {

	private final ArrayList<Action> actions;
	
	public DefaultObjectBuilder() {
		actions = new ArrayList<>();
	}
	
	@Override
	public boolean transaction(BooleanSupplier action) {
		int initialSize = actions.size();
		
		boolean result = action.getAsBoolean();
		
		if (!result) {
			// rollback
			for (int i = actions.size() - 1; i >= initialSize; i--) {
				actions.remove(i);
			}
		}
		
		return result;
	}
	
	@Override
	public void openElement() {
		actions.add(new OpenElement());	
	}
	
	@Override
	public void setAttribute(String name, Object value, boolean array) {
		actions.add(new SetAttribute(name, value, array));
	}
	
	@Override
	public void closeElement(String name, boolean array) {
		actions.add(new CloseElement(name, array));
	}
	
	@Override
	public Map<String, Object> build() {
		Stack<Map<String,Object>> stack = new Stack<>();
		
		stack.push(new LinkedHashMap<>());
		
		for (Action action : actions) {
			try {
				action.apply(stack);
			}
			catch (EmptyStackException e) {
				throw new RuntimeException(e); // TODO add error message
			}
		}
		
		// check stack integrity
		if (stack.size() != 1) {
			throw new IllegalStateException(); // TODO add error message
		}
		
		return stack.pop();
	}
	
	private static abstract class Action {
		
		abstract public void apply(Stack<Map<String, Object>> stack);
		
	}
	
	private static class OpenElement extends Action {

		@Override
		public void apply(Stack<Map<String, Object>> stack) {
			stack.push(new LinkedHashMap<>());
		}
		
	}
	
	private static class SetAttribute extends Action {

		public final String name;
		public final Object value;
		public final boolean array;
		
		public SetAttribute(String name, Object value, boolean array) {
			this.name = name;
			this.value = value;
			this.array = array;
		}
		
		@Override
		public void apply(Stack<Map<String, Object>> stack) {
			Map<String, Object> element = stack.peek();
			
			assign(element, name, value, array);
		}
		
	}
	
	private static class CloseElement extends Action {

		public String name;
		public boolean array;
		
		public CloseElement(String name, boolean array) {
			this.name = name;
			this.array = array;
		}
		
		@Override
		public void apply(Stack<Map<String, Object>> stack) {
			Map<String, Object> element = stack.pop();
			Map<String, Object> parent = stack.peek();
			
			assign(parent, name, element, array);
		}
		
	}
	
	private static void assign(Map<String, Object> element, String name, Object value, boolean array) {
		if (array) {
			ObjectList list;
			
			if (element.containsKey(name)) {
				Object currentValue = element.get(name);
				
				if (currentValue instanceof ObjectList) {
					list = (ObjectList)currentValue;
				} else {
					list = new ObjectList();
					
					list.add(currentValue);
				}				
			} else {
				list = new ObjectList();
			}
			
			list.add(value);
			
			element.put(name, list);
		} else {
			if (element.containsKey(name)) {
				throw new RuntimeException("already defined: " + name);
			}
			
			element.put(name, value);
		}
	}
	
	private static class ObjectList extends ArrayList<Object> {

		private static final long serialVersionUID = 3537400436947045419L;
		
	}

}
