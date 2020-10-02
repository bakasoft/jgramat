package gramat.util;

import java.util.LinkedHashMap;

public class NameMap<T> extends LinkedHashMap<String, T> {

    public T find(String name) {
        var value = get(name);

        if (value == null) {
            throw new RuntimeException("Not found: " + name);
        }

        return value;
    }

    public void set(String name, T value) {
        if (get(name) != null) {
            throw new RuntimeException("Already exists: " + name);
        }

        put(name, value);
    }
}
