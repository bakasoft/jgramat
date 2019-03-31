package org.bakasoft.gramat.handlers;

public class DefaultHandler implements ObjectHandler {

    private final DefaultMap map;

    public DefaultHandler() {
        map = new DefaultMap();
    }

    @Override
    public Object getInstance() {
        return map;
    }

    @Override
    public Class<?> getType() {
        return DefaultMap.class;
    }

    @Override
    public Class<?> getPropertyType(String name) {
        return Object.class;
    }

    @Override
    public void setValue(String name, Object value) {
        map.put(name, value);
    }

    @Override
    public void addValue(String name, Object value) {
        Object current = map.get(name);
        DefaultList list;

        if (current != null) {
            if (!(current instanceof DefaultList)) {
                throw new RuntimeException("expected default list: " + name);
            }

            list = (DefaultList)current;
        }
        else {
            list = new DefaultList();

            map.put(name, list);
        }

        list.add(value);
    }

}
