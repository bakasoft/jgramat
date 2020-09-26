package gramat.capturing;

import gramat.Debug;
import gramat.parsers.ValueParser;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class Assembler {

    private final Stack<Object> valueStack;
    private final Stack<String> nameStack;
    private final LinkedHashMap<String, Object> attributeMap;

    public Assembler() {
        valueStack = new Stack<>();
        nameStack = new Stack<>();
        attributeMap = new LinkedHashMap<>();
    }

    public void pushValue(String text, ValueParser parser) {
        pushValue(parser != null ? parser.parse(text) : text);
    }

    public void pushValue(Object value) {
        Debug.log("Push value: " + value);

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
        expectEmptyValues();

        if (attributeMap.size() > 0) {
            throw new RuntimeException("unexpected attributes");
        }
    }

    public void expectEmptyValues() {
        if (valueStack.size() > 0) {
            throw new RuntimeException("unexpected values");
        }
    }

    public boolean isEmpty() {
        if (valueStack.size() > 0) {
            return false;
        }

        if (nameStack.size() > 0) {
            return false;
        }

        if (attributeMap.size() > 0) {
            return false;
        }

        return true;
    }

    public void setAttribute(String name, Object value) {
        Debug.log("Set attribute: " + name + "=" + value);

        if (attributeMap.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        attributeMap.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

    public void pushName(String name) {
        nameStack.push(name);
    }

    public String popName() {
        return nameStack.pop();
    }
}
