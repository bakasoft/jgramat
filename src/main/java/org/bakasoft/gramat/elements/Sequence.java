package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Sequence extends Element {

    private final Element[] elements;

    public Sequence(Element[] elements) {
        this.elements = elements;
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return control.isCyclic(elements);
    }

    @Override
    public boolean parse(Tape tape) {
        int pos0 = tape.getPosition();

        for (Element element : elements) {
            if (!element.parse(tape)) {
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
            for (int i = 0; i < elements.length; i++) {
                if (i > 0) {
                    output.append(' ');
                }

                elements[i].codify(control, false);
            }
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            if (elements.length > 0) {
                elements[0].collectFirstAllowedSymbol(control, symbols);
            }
        });
    }

}
