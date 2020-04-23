package gramat.values;

import java.util.HashMap;

abstract public class ObjectValue implements Value {

    private HashMap<String, Value> attributes;

    protected HashMap<String, Value> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    public void set(String name, Value value) {
        getAttributes().put(name, value);
    }

}
