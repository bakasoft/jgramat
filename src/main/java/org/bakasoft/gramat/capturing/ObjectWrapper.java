package org.bakasoft.gramat.capturing;

public interface ObjectWrapper {

    Object getInstance();

    void setValue(String name, Object value);

    void addValue(String name, Object value);
}
