package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;

import java.util.Set;

public class CharRange extends Element {

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
    public Object capture(Tape tape) {
        return captureText(tape);
    }

    @Override
    public void collectFirstAllowedSymbol(CyclicControl control, Set<String> symbols) {
        // no symbols to collect
    }
}
