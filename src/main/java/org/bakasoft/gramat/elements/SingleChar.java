package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class SingleChar extends Element implements WrappedElement {

    private final char c;

    public SingleChar(char c) {
        this.c = c;
    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        // nothing to be replaced
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return false;
    }

    @Override
    public void optimize(OptimizationControl control) {
        // nothing to be optimized
    }

    @Override
    public boolean parse(Tape tape) {
        if (tape.alive()) {
            char actual = tape.peek();

            if (actual == c) {
                tape.moveForward();
                return tape.ok(this);
            }
        }

        return tape.no(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append('"');
            output.append(c);
            output.append('"');
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            symbols.add(String.valueOf(c));
        });
    }

}
