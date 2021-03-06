package org.gramat.elements;

import org.bakasoft.framboyan.inspect.Inspector;

import java.util.Map;
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
                ctx.capture.append(c);
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
        symbols.add(Inspector.inspect(c));
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        // nothing to be linked
    }

}
