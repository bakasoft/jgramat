package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Optional;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GOptional extends GElement {

    public final GElement expression;

    public GOptional(GElement expression) {
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public GElement simplify() {
        GElement simplification = expression.simplify();

        if (simplification == null) {
            return null;
        }
        // double optional simplification
        else if (simplification instanceof GOptional) {
            return simplification;
        }

        return new GOptional(simplification);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Optional(expression.compile(gramat, compiled));
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return expression.isPlain(gramat);
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return true;
    }

    public static GOptional expectOptional(Tape tape) {
        expectSymbol(tape, '[');

        skipVoid(tape);

        GElement expression = expectExpression(tape);

        skipVoid(tape);

        expectSymbol(tape, ']');

        return new GOptional(expression);
    }

}
