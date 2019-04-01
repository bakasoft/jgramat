package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Map;
import java.util.Set;

public class Negation extends Element {

    private Element element;

    public Negation(Element element) {
        this.element = element;
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();

        if (element.parse(tape)) {
            // did not match!
            tape.setPosition(pos0);
            return tape.no(this);
        }

        if (tape.alive()) {
            // perfect match!
            tape.moveForward();
            return tape.ok(this);
        }

        tape.setPosition(pos0);
        return tape.no(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public Element link() {
        return new Negation(element.link());
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        // no symbols to collect
    }

}
