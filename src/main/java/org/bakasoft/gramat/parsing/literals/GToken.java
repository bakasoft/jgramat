package org.bakasoft.gramat.parsing.literals;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.elements.GString;

import java.util.*;

public class GToken implements GLiteral {

    private final String content;

    public GToken(String content) {
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public GToken forceToken() {
        return this;
    }

    @Override
    public GArray forceArray() {
        GArray array = new GArray();
        array.add(this);
        return array;
    }

    @Override
    public GMap forceMap() {
        GMap map = new GMap();
        map.put(null, this);
        return map;
    }

    @Override
    public String forceString() {
        return content;
    }

    @Override
    public List<String> forceStringList() {
        return Collections.singletonList(content);
    }

    @Override
    public Map<String, String> forceStringMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(null, content);
        return map;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return content.equals(obj);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
