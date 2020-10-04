package gramat.eval;

import gramat.framework.Logger;
import gramat.parsers.ValueParser;

import java.util.*;

public class Container {

    private List<Object> values;
    private Map<String, Object> attributes;

    private String name;

    public Container() {
        this.values = new LinkedList<>();
        this.attributes = new LinkedHashMap<>();
    }

    public void addValue(Object value) {
        values.add(value);
    }

    public void addAttribute(String name, Object value) {
        if (attributes.containsKey(name)) {
            throw new RuntimeException("attribute already defined: " + name);
        }

        attributes.put(name, value);
    }

    public void addName(String name) {
        if (this.name != null) {
            throw new RuntimeException();
        }
        this.name = name;
    }

    public void expectEmpty() {
        if (values != null && values.size() > 0) {
            throw new RejectedException("unexpected values");
        }

        if (name != null) {
            throw new RejectedException("unexpected name");
        }

        if (attributes != null && attributes.size() > 0) {
            throw new RejectedException("unexpected attributes");
        }
    }

    public Map<String, ?> removeAttributes() {
        var aux = attributes;

        attributes = null;

        return aux;
    }

    public List<Object> removeValues() {
        var aux = values;

        values = null;

        return aux;
    }

    public String removeName() {  // TODO should this be remove?
        var aux = name;
        name = null;
        return aux;
    }
}