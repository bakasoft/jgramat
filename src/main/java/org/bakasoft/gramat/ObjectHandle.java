package org.bakasoft.gramat;

import java.beans.PropertyDescriptor;
import java.util.Map;

public class ObjectHandle {

    private final Class<?> type;
    private final Map<String, PropertyDescriptor> properties;
    private final Object instance;

    public ObjectHandle(Class<?> type, Object instance) {
        this.type = type;
        this.instance = instance;
        this.properties = ReflectionHelper.loadProperties(type, true, true);
    }

    public Object getInstance() {
        return instance;
    }

    public Class<?> getType() { return type; }

    public Class<?> getPropertyType(String name) {
        PropertyDescriptor property = properties.get(name);

        if (property == null) {
            throw new RuntimeException("unknown property: " + name);
        }

        return property.getPropertyType();
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
