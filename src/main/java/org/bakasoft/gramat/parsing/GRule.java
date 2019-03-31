package org.bakasoft.gramat.parsing;

import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.elements.GObject;

import java.util.Map;
import java.util.Objects;

public class GRule {

    public final String name;
    public final boolean objectMode;
    public final GElement expression;

    public GRule(String name, boolean objectMode, GElement expression) {
        this.name = Objects.requireNonNull(name);
        this.objectMode = objectMode;
        this.expression = Objects.requireNonNull(expression);
    }

    public GRule simplify() {
        return new GRule(name, objectMode, expression.simplify());
    }

    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        if (objectMode) {
            GObject obj = new GObject(name, expression);

            return obj.compile(gramat, compiled);
        }

        return expression.compile(gramat, compiled);
    }

    public static GRule expectRule(Tape tape) {
        String name = GElement.expectName(tape, "rule name");

        GElement.skipVoid(tape);

        boolean objectMode;

        if (GElement.trySymbols(tape, ":=")) {
            objectMode = true;
        }
        else if (GElement.trySymbol(tape, '=')) {
            objectMode = false;
        }
        else {
            throw new GrammarException("Expected rule assignment: " + GElement.inspect(":=") + " or " + GElement.inspect("="), tape.getLocation());
        }

        GElement.skipVoid(tape);

        GElement expression = GElement.expectExpression(tape);

        return new GRule(name, objectMode, expression);
    }

}
