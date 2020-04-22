package gramat.values;

import gramat.GramatException;

import java.util.HashMap;

abstract public class ObjectValue implements Value {

    private HashMap<String, Value> attributes;

    protected HashMap<String, Value> getAttributes() {
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        return attributes;
    }

    @Override
    public void concat(ConcatenatedValue value) {
        throw new GramatException("cannot concatenate an object");
    }

    public void set(String name, Value value) {
        getAttributes().put(name, value);
    }

}
