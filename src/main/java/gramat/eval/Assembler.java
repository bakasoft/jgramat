package gramat.eval;

import gramat.compiling.ValueParser;

import java.util.*;

public class Assembler {

    private final Stack<Object> valueStack;
    private final LinkedHashMap<String, Object> attributeMap;

    public Assembler() {
        valueStack = new Stack<>();
        attributeMap = new LinkedHashMap<>();
    }

    public void pushValue(String text, ValueParser parser) {
        pushValue(parser != null ? parser.parse(text) : text);
    }

    public void pushValue(Object value) {
        System.out.println("PUSH VALUE: " + value);
        valueStack.push(value);
    }

    public String popString() {
        if (valueStack.isEmpty()) {
            throw new RuntimeException();
        }
        var value = valueStack.pop();

        if (!(value instanceof String)) {
            throw new RuntimeException();
        }

        return (String)value;
    }

    public Object popValue() {
        if (valueStack.isEmpty()) {
            throw new RuntimeException();
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

    public void setAttribute(String name, Object value) {
        if (attributeMap.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        System.out.println("SET ATTR " + name + " = " + value);

        attributeMap.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

}
