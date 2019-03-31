package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class SingleChar extends Element {

    private final char c;

    public SingleChar(char c) {
        this.c = c;
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
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            symbols.add(String.valueOf(c));
        });
    }

}
