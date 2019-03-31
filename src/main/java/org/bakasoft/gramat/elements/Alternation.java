package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Map;
import java.util.Set;

public class Alternation extends Element {

    private final Element[] elements;

    public Alternation(Element[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();

        for (Element element : elements) {
            if (element.parse(tape)) {
                // perfect match!
                return tape.ok(this);
            }
            else {
                // try with another!
                tape.setPosition(pos0);
            }
        }

        // did not match!
        return tape.no(this);
    }

    @Override
    public Object capture(Tape tape) {
        int pos0 = tape.getPosition();

        for (Element element : elements) {
            Object result = element.capture(tape);

            if (result != null) {
                // perfect match!
                return result;
            }
            else {
                // try with another!
                tape.setPosition(pos0);
            }
        }

        // did not match!
        return null;
    }

    @Override
    public Element link() {
        return new Alternation(linkAll(elements));
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            for (Element element : elements) {
                element.collectFirstAllowedSymbol(control, symbols);
            }
        });
    }
}
