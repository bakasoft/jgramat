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

    @Override
    public void optimize(OptimizationControl control) {
        control.enter(this, () -> {
            if (elements.length == 1) {
                control.apply("reduce single element sequence", this, elements[0]);
            }
    //        else if (Sets.containsRule(elements, Sequence.class)) {
    //            ArrayList<Element> result = new ArrayList<>();
    //
    //            for (Element element : elements) {
    //                if (element instanceof Sequence) {
    //                    Sequence seq = (Sequence) element;
    //
    //                    Collections.addAll(result, seq.elements);
    //
    //                    control.increase("nested sequence");
    //                } else {
    //                    result.addRule(element);
    //                }
    //            }
    //
    //            return new Sequence(result.toArray(new Element[0]));
    //        }
            else {
                control.next(elements);
            }
        });
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
