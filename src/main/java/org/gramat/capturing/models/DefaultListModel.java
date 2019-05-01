package org.gramat.capturing.models;

import org.gramat.capturing.DefaultList;
import org.gramat.capturing.ObjectModel;

public class DefaultListModel implements ObjectModel {

    private final DefaultList list;

    public DefaultListModel() {
        list = new DefaultList();
    }

    @Override
    public Object getInstance() {
        return list;
    }

    @Override
    public void setValue(String name, Object value) {
        throw new RuntimeException("can't set properties to lists");
    }

    @Override
    public void addValue(String name, Object value) {
        throw new RuntimeException("can't set properties to lists");
    }

    @Override
    public void addValue(Object value) {
        list.add(value);
    }

}
