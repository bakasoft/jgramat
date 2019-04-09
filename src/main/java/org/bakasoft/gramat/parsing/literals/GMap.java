package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GMap extends GLiteral {

    private final Map<String, GLiteral> map;

    public GMap(Map<String, GLiteral> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public GLiteral get(String key) {
        return map.get(key);
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Map<String, GLiteral> getMap() {
        return map;
    }

}
