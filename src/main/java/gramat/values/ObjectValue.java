package gramat.values;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class ObjectValue implements Value {

    private LinkedHashMap<String, Value> attributes;

    protected Map<String, Value> getAttributes() {
        if (attributes == null) {
            attributes = new LinkedHashMap<>();
        }
        return attributes;
    }

    public void set(String name, Value value) {
        getAttributes().put(name, value);
    }

}
