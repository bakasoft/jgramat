package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Nop extends Element {

    @Override
    public boolean parse(Tape tape) {
        return true;
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {

    }

    @Override
    public Object capture(Tape tape) {
        return null;
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {

    }
}
