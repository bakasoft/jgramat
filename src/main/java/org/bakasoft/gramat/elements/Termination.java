package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.Grammar;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.parsing.TerminationData;

import java.util.Set;

public class Termination extends Element implements WrappedElement {

    // TODO what happen with the constructors?
    // TODO does this be mapped to `$`?

    public Termination() {

    }

    @Override
    public void replace(CyclicControl control, Element older, Element newer) {
        // nothing to be replaced
    }

    @Override
    public boolean isCyclic(CyclicControl control) {
        return false;
    }

    @Override
    public void optimize(OptimizationControl control) {
        // nothing to be optimized
    }

    public Termination(Grammar grammar, TerminationData data) {
        grammar.addElement(data, this);
    }

    @Override
    public boolean parse(Tape tape) {
        if (tape.alive()) {
            return tape.no(this);

        }

        return tape.ok(this);
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append("end");
        });
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        // nothing to collect
    }

}
