package gramat.engine.actions.capturing;

import gramat.engine.parsers.ValueParser;
import gramat.tools.Debug;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class ValueAssembler {

    private final Stack<Object> valueStack;
    private final LinkedHashMap<String, Object> attributeMap;

    public ValueAssembler() {
        valueStack = new Stack<>();
        attributeMap = new LinkedHashMap<>();
    }

    public void pushValue(String text, ValueParser parser) {
        pushValue(parser != null ? parser.parse(text) : text);
    }

    public void pushValue(Object value) {
        Debug.log("Push value");

        valueStack.push(value);
    }

    public String popString() {
        var value = popValue();

        if (!(value instanceof String)) {
            throw new RuntimeException();
        }

        return (String)value;
    }

    public Object popValue() {
        Debug.log("Pop value");

        if (valueStack.isEmpty()) {
            throw new RuntimeException("empty stack");
        }
        return valueStack.pop();
    }

    public void expectEmpty() {
        if (valueStack.size() > 0) {
            throw new RuntimeException();
        }

        if (attributeMap.size() > 0) {
            throw new RuntimeException("unexpected attributes");
        }
    }

    public boolean isEmpty() {
        if (valueStack.size() > 0) {
            return false;
        }

        if (attributeMap.size() > 0) {
            return false;
        }

        return true;
    }

    public void setAttribute(String name, Object value) {
        Debug.log("Set attribute: " + name);

        if (attributeMap.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        attributeMap.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

}
