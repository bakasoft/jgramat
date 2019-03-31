package org.bakasoft.gramat.handlers;

import java.beans.PropertyDescriptor;
import java.util.Map;

public interface ObjectHandler {

    Object getInstance();

    Class<?> getType();

    Class<?> getPropertyType(String name);

    void setValue(String name, Object value);

    void addValue(String name, Object value);
}
