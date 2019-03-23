package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.OptionalData;

import java.util.Set;

public class Optional extends Element implements WrappedElement {

    private Element element;

    public Optional(Element element) {
        this.element = element;
    }

    public Optional(Grammar grammar, OptionalData data) {
        grammar.addElement(data, this);

        element = grammar.settle(data.getExpression());
    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        control.enter(this, () -> {
            if (element == older) {
                element = newer;
            }
            else {
                element.replace(control, older, newer);
            }
        });
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return control.isCyclic(element);
    }

    @Override
    public void optimize(OptimizationControl control) {
        control.enter(this, () -> {
            if (element instanceof Optional) {
                control.apply("remove double optional", this, element);
            }
            else {
                control.next(element);
            }
        });
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
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append('[');
            element.codify(control, true);
            output.append(']');
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            element.collectFirstAllowedSymbol(control, symbols);
        });
    }
}