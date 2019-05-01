package org.bakasoft.gramat.capturing.models;

import org.bakasoft.gramat.capturing.DefaultList;
import org.bakasoft.gramat.capturing.DefaultMap;
import org.bakasoft.gramat.capturing.ObjectModel;

public class DefaultMapModel implements ObjectModel {

    private final DefaultMap map;

    public DefaultMapModel() {
        map = new DefaultMap();
    }

    @Override
    public Object getInstance() {
        return map;
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

    @Override
    public void addValue(Object value) {
        throw new RuntimeException("can't add items to map");
    }

}
