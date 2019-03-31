package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Termination extends Element {

    // TODO what happen with the constructors?
    // TODO does this be mapped to `$`?

    public Termination() {

    }

    @Override
    public boolean parse(Tape tape) {
        if (tape.alive()) {
            return tape.no(this);

        }

        return tape.ok(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public Element link() {
        return this;
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        // nothing to collect
    }

}
