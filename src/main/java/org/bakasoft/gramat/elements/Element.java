package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.*;
import org.bakasoft.gramat.parsing.elements.GElement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

abstract public class Element {

    public static Element eval(String code) {
        return GElement.expectExpression(new Tape(null, code))
                .simplify()
                .compile(new Gramat(), new HashMap<>());
    }

    abstract protected boolean parseImpl(Context ctx);

    abstract public boolean isOptional(Set<Element> control);

    abstract public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols);

    abstract public Element link();

    public final Object capture(Tape tape) {
        Context ctx = new Context(tape);

        if (parse(ctx)) {
            if (tape.alive()) {
                throw new GrammarException("expected end of string", tape.getLocation());
            }

            return ctx.builder.pop();
        }

        throw new GrammarException(ctx.errors.getMessage(), ctx.errors.getLocation());
    }

    public final boolean parse(Context ctx) {
        ctx.builder.beginTransaction();
        ctx.capture.beginTransaction();

        if (parseImpl(ctx)) {
            ctx.builder.commitTransaction();
            ctx.capture.commitTransaction();
            ctx.errors.flush();
            return true;
        }
        else {
            ctx.builder.rollbackTransaction();
            ctx.capture.rollbackTransaction();
            ctx.errors.log(this);
            return false;
        }
    }

    public static Element[] linkAll(Element[] elements) {
        Element[] linked = new Element[elements.length];

        for (int i = 0; i < elements.length; i++) {
            linked[i] = elements[i].link();
        }

        return linked;
    }

    public boolean isOptional() {
        return isOptional(new HashSet<>());
    }

    public Set<String> collectFirstAllowedSymbol() {
        HashSet<String> symbols = new HashSet<>();

        collectFirstAllowedSymbol(new HashSet<>(), symbols);

        return symbols;
    }
}
