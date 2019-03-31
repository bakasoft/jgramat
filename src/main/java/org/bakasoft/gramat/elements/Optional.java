package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Optional extends Element implements WrappedElement {

    private Element element;

    public Optional(Element element) {
        this.element = element;
    }

    @Override
    public boolean parse(Tape tape) {
        element.parse(tape);

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
