package gramat.eval;

import gramat.compiling.ValueParser;
import gramat.util.Debugger;

import java.util.*;

public class Assembler {

    private final Debugger debugger;
    private final Stack<Object> valueStack;
    private final LinkedHashMap<String, Object> attributeMap;

    public Assembler(Debugger debugger) {
        this.debugger = debugger;
        valueStack = new Stack<>();
        attributeMap = new LinkedHashMap<>();
    }

    public void pushValue(String text, ValueParser parser) {
        pushValue(parser != null ? parser.parse(text) : text);
    }

    public void pushValue(Object value) {
        debugger.log("PUSH VALUE: " + value);
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
        debugger.log("POP VALUE");
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
        debugger.log("SET ATTR " + name + " = " + value);

        if (attributeMap.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        attributeMap.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

}
