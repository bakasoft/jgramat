package org.bakasoft.gramat.elements;

import org.bakasoft.gramat.*;
import org.bakasoft.gramat.parsers.Parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

abstract public class Element {

    public static Element eval(String code) {
        Gramat gramat = new Gramat();
        return Parser.expectExpression(gramat, new Tape(null, code))
                .simplify()
                .compile(new HashMap<>());
    }

    abstract protected boolean parseImpl(Context ctx);

    abstract public boolean isOptional(Set<Element> control);

    abstract public void collectFirstAllowedSymbol(Set<Element> control, Set<String> symbols);

    abstract public void resolveInto(Map<String, Element> rules, Set<Element> control);

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

    public static void resolveAllInto(Map<String, Element> rules, Set<Element> control, Element[] elements) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = resolveInto(rules, control, elements[i]);
        }
    }

    public static Element resolveInto(Map<String, Element> rules, Set<Element> control, Element element) {
        if (element == null) {
            return null;
        }
        else if (element instanceof Reference) {
            String name = ((Reference) element).getName();
            Element target = rules.get(name);

            if (target == null) {
                throw new RuntimeException("rule not found: " + name);
            }
            else if (target instanceof Reference) {
                Reference targetRef = (Reference)target;
                Element targetResolved = resolveInto(rules, control, targetRef);
                rules.put(name, targetResolved);
                return targetResolved;
            }

            return target;
        } else {
            element.resolveInto(rules, control);
        }

        return element;
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
