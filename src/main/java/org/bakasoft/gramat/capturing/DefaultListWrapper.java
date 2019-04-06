package org.bakasoft.gramat.capturing;

public class DefaultListWrapper implements ObjectWrapper {

    private final DefaultList list;

    public DefaultListWrapper() {
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
