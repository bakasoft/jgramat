package org.bakasoft.gramat.elements;

import java.util.Map;
import java.util.Set;

public class Termination extends Element {

    public Termination() {}

    @Override
    protected boolean parseImpl(Context ctx) {
        if (ctx.tape.alive()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        symbols.add("end of string");
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        // nothing to be linked
    }

}
