package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Symbol extends Element implements WrappedElement {

    private final String symbol;

    public Symbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return false;
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();
        int index = 0;

        while (index < symbol.length()) {
            char expected = symbol.charAt(index);

            if (tape.alive()) {
                char actual = tape.peek();
                if (expected == actual) {
                    index++;
                    tape.moveForward();
                }
                else {
                    // did not match!
                    tape.setPosition(pos0);
                    return tape.no(this);
                }
            }
            else {
                // did not match!
                tape.setPosition(pos0);
                return tape.no(this);
            }
        }

        // perfect match!
        return tape.ok(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append('"');
            output.append(symbol);
            output.append('"');
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            symbols.add(symbol);
        });
    }

}
