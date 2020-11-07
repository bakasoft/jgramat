package stone.examiners.impl;

import stone.errors.StoneException;
import stone.examiners.ObjectExaminer;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GenericMapExaminer implements ObjectExaminer {

    public static final GenericMapExaminer ANONYMOUS_INSTANCE = new GenericMapExaminer(null);

    private final String type;

    public GenericMapExaminer(String type) {
        this.type = type;
    }

    private static Map<?,?> map(Object any) {
        return (Map<?,?>)any;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Set<String> getKeys(Object value) {
        var keys = new LinkedHashSet<String>();

        for (var key : map(value).keySet()) {
            if (key instanceof String) {
                keys.add(((String) key));
            }
            else {
                throw new StoneException();
            }
        }

        return keys;
    }

    @Override
    public Object getValue(Object value, String key) {
        return map(value).get(key);
    }
}
