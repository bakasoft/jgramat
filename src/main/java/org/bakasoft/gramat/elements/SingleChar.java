package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Stringifier;

import java.util.Set;

public class SingleChar extends Element {

    private final char c;

    public SingleChar(char c) {
        this.c = c;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        if (ctx.tape.alive()) {
            char actual = ctx.tape.peek();

            if (actual == c) {
                ctx.tape.moveForward();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        symbols.add(Stringifier.literal(String.valueOf(c)));
    }

    @Override
    public Element link() {
        return this;
    }

}
