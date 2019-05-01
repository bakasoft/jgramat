package org.gramat.elements;

import java.util.Map;
import java.util.Set;

public class Reference extends Element {

    private final String name;

    public Reference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        throw new RuntimeException("unresolved element: " + name);
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        throw new RuntimeException("unresolved element: " + name);
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        throw new RuntimeException("unresolved element: " + name);
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        throw new RuntimeException("unresolved element: " + name);
    }
}
