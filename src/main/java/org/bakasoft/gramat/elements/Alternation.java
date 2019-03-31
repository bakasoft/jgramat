package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Tape;

import java.util.Set;

public class Alternation extends Element implements WrappedElement {

    private final Element[] elements;

    public Alternation(Element[] elements) {
        this.elements = elements;
    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        control.enter(this, () -> {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] == older) {
                    elements[i] = newer;
                }
                else {
                    elements[i].replace(control, older, newer);
                }
            }
        });
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return control.isCyclic(elements);
    }

    public void optimize(OptimizationControl control) {
        control.enter(this, () -> {
            if (elements.length == 1) {
                control.apply("reduce single element alternation",this, elements[0]);
            }
            else {
                control.next(elements);
            }
        });
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
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            for (int i = 0; i < elements.length; i++) {
                if (i > 0) {
                    output.append('|');
                }

                elements[i].codify(control, false);
            }
        });
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
