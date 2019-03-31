package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Repetition extends Element {

    private Element element;
    private final int minimum;
    private final int maximum;
    private Element separator;

    public Repetition(Element element, int minimum, int maximum, Element separator) {
        this.element = element;
        this.minimum = minimum;
        this.maximum = maximum;
        this.separator = separator;
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();
        boolean expectMore = false;
        int count = 0;

        while (element.parse(tape)) {
            if (separator != null) {
                if (separator.parse(tape)) {
                    expectMore = true;
                } else {
                    expectMore = false;
                }
            }
            else {
                expectMore = false;
            }

            count++;
        }

        if (expectMore) {
            // did not match!
            tape.setPosition(pos0);
            return tape.no(this);
        }
        else if (count < minimum) {
            // did not match!
            tape.setPosition(pos0);
            return tape.no(this);
        }
        else if (maximum > 0 && count > maximum) {
            // did not match!
            tape.setPosition(pos0);
            return tape.no(this);
        }

        // perfect match!
        return tape.ok(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            element.collectFirstAllowedSymbol(control, symbols);
        });
    }
}
