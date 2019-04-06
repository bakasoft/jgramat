package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GElement;
import org.bakasoft.gramat.parsing.elements.captures.GObject;

import java.util.Map;
import java.util.Objects;

public class GRule {

    public final String name;
    public final GElement expression;

    public GRule(String name, GElement expression) {
        this.name = Objects.requireNonNull(name);
        this.expression = Objects.requireNonNull(expression);
    }

    public GRule simplify() {
        GElement simplified = expression.simplify();

        if (simplified == null) {
            return null;
        }

        return new GRule(name, simplified);
    }

    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        // TODO create rules according to the expression
        throw new UnsupportedOperationException();
    }

    public static GRule expectRule(Tape tape) {
        String name = GElement.expectName(tape, "rule name");

        GElement.skipVoid(tape);

        if (!GElement.trySymbol(tape, '=')) {
            throw new GrammarException("Expected rule assignment: " + GElement.inspect("="), tape.getLocation());
        }

        GElement.skipVoid(tape);

        GElement expression = GElement.expectExpression(tape);

        return new GRule(name, expression);
    }

}
