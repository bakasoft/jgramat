package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Stringifier;

import java.util.Set;

public class Symbol extends Element {

    private final String symbol;

    public Symbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        int pos0 = ctx.tape.getPosition();
        int index = 0;

        while (index < symbol.length()) {
            char expected = symbol.charAt(index);

            if (ctx.tape.alive()) {
                char actual = ctx.tape.peek();
                if (expected == actual) {
                    index++;
                    ctx.tape.moveForward();
                }
                else {
                    // did not match!
                    ctx.tape.setPosition(pos0);
                    return false;
                }
            }
            else {
                // did not match!
                ctx.tape.setPosition(pos0);
                return false;
            }
        }

        // perfect match!
        return true;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        symbols.add(Stringifier.literal(symbol));
    }

    @Override
    public Element link() {
        return this;
    }

}
