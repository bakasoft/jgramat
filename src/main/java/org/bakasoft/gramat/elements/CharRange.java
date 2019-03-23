package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;

import java.util.Set;

public class CharRange extends Element implements WrappedElement {

    private final String description; // TODO name?
    private final CharPredicate predicate;

    public CharRange(String description, CharPredicate predicate) {
        this.description = description;
        this.predicate = predicate;

        if (predicate == null) {
            throw new GrammarException();
        }
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

    @Override
    public boolean parse(Tape tape) {
        if (tape.alive()) {
            char actual = tape.peek();

            if (predicate.test(actual)) {
                // perfect match!
                tape.moveForward();
                return tape.ok(this);
            }
        }

        // did not match!
        return tape.no(this);
    }

    @Override
    public void codify(CodifyControl control, boolean grouped) {
        control.codify(this, grouped, output -> {
            output.append(description);
        });
    }

    @Override
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        // no symbols to collect
    }
}
