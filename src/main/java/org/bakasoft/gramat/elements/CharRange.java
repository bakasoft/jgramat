package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.CharPredicate;
import org.bakasoft.gramat.GrammarException;

import java.util.Map;
import java.util.Set;

public class CharRange extends Element {

    private final String name;
    private final CharPredicate predicate;

    public CharRange(String name, CharPredicate predicate) {
        this.name = name;
        this.predicate = predicate;

        if (predicate == null) {
            throw new RuntimeException();
        }
    }

    @Override
    protected boolean parseImpl(Context ctx) {
        if (ctx.tape.alive()) {
            char actual = ctx.tape.peek();

            if (predicate.test(actual)) {
                // perfect match!
                ctx.capture.append(actual);
                ctx.tape.moveForward();
                return true;
            }
        }

        // did not match!
        return false;
    }

    @Override
    public boolean isOptional(Set<Element> control) {
        return false;
    }

    @Override
    public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols) {
        symbols.add(name);
    }

    public void resolveInto(Map<String, Element> rules, Set<Element> control) {
        // nothing to be linked
    }

    public String getName() {
        return name;
    }

    public CharPredicate getPredicate() {
        return predicate;
    }
}
