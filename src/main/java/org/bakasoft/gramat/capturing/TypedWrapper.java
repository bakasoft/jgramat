package org.bakasoft.gramat.capturing;

import org.bakasoft.gramat.util.ReflectionHelper;

import java.beans.PropertyDescriptor;
import java.util.Map;

public class TypedWrapper implements ObjectWrapper {

    private final Class<?> type;
    private final Map<String, PropertyDescriptor> properties;
    private final Object instance;

    public TypedWrapper(Class<?> type) {
        this.type = type;
        this.instance = ReflectionHelper.newInstance(type);
        this.properties = ReflectionHelper.loadProperties(type, true, true);
    }

    public Object getInstance() {
        return instance;
    }

    public void setValue(String name, Object value) {
        PropertyDescriptor property = properties.get(name);

        if (property == null) {
            throw new RuntimeException("unknown property: " + type + "#" + name);
        }

        try {
            ReflectionHelper.setValue(property, instance, value);
        }
        catch (Exception e) {
            throw new RuntimeException("cannot assign " + type + "#" + name + ": " + e.getMessage(), e);
        }
    }

    public void addValue(String name, Object value) {
        PropertyDescriptor property = properties.get(name);

        if (property == null) {
            throw new RuntimeException("unknown property: " + type + "#" + name);
        }

        ReflectionHelper.addElement(property, instance, value);
    }
}
