package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.RuleData;

import java.util.Set;

public class RuleElement extends Element implements WrappedElement {

    private final String name;
    private Element element;

    public RuleElement(Grammar grammar, RuleData data) {
        grammar.addElement(data, this);

        this.name = data.getName();
        this.element = grammar.settle(data.getExpression());
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
            control.next(element);
        });
    }

    public boolean parse(Tape tape) {
        return element.parse(tape);
    }

    @Override
    public Object capture(Tape tape) {
        return element.capture(tape);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            // TODO name?
            element.codify(control, grouped);
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        control.enter(this, () -> {
            element.collectFirstAllowedSymbol(control, symbols);
        });
    }

}
