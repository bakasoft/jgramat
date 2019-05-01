package org.gramat.elements;

import org.bakasoft.framboyan.inspect.Inspector;

import java.util.Map;
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
        ctx.capture.append(symbol);
        return true;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        symbols.add(Inspector.inspect(symbol));
    }

    @Override
    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        // nothing to be linked
    }

}
