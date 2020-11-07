package stone.common;

import stone.errors.StoneException;

import java.util.LinkedHashMap;

public class GenericObject extends LinkedHashMap<String, Object> {

    private final String type;

    public GenericObject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void set(String key, Object value) {
        if (containsKey(key)) {
            throw new StoneException();
        }
        put(key, value);
    }
}
