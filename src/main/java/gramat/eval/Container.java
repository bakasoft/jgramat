package gramat.eval;

import gramat.parsers.ValueParser;

import java.util.*;

public class Container {

    private final Context context;
    private final Stack<Object> valueStack;
    private final LinkedHashMap<String, Object> attributeMap;

    private String name;

    public Container(Context context) {
        this.context = context;
        valueStack = new Stack<>();
        attributeMap = new LinkedHashMap<>();
    }

    public void pushValue(String text, ValueParser parser) {
        pushValue(parser != null ? parser.parse(text) : text);
    }

    public void pushValue(Object value) {
        context.logger.debug("push value %s", value);

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
        context.logger.debug("pop value");

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
        context.logger.debug("set attribute %s=%s", name, value);

        if (attributeMap.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        attributeMap.put(name, value);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributeMap);
    }

    public void pushName(String name) {
        context.logger.debug("push name %s", name);
        if (this.name != null) {
            throw new RuntimeException();
        }
        this.name = name;
    }

    public String popName(String defaultName) {
        context.logger.debug("pop name (default=%s)", defaultName);
        if (defaultName != null && name != null) {
            throw new RuntimeException("conflicted name");
        }
        else if (defaultName != null) {
            return defaultName;
        }
        return name;
    }

    public void expectEmptyValues() {
        if (valueStack.size() > 0) {
            throw new RuntimeException();
        }
    }

    public List<Object> popArray() {
        context.logger.debug("pop array");
        var items = new ArrayList<>();

        while (valueStack.size() > 0) {
            var value = valueStack.pop();

            items.add(value);
        }

        return items;
    }
}