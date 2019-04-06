package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Negation;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GNegation extends GElement {

    public final GElement expression;

    public GNegation(GElement expression) {
        this.expression = Objects.requireNonNull(expression);
    }

    @Override
    public GElement simplify() {
        GElement simplification = expression.simplify();

        if (simplification == null) {
            return null;
        }

        // remove double negation
        else if (simplification instanceof GNegation) {
            return ((GNegation)simplification).expression;
        }

        return new GNegation(simplification);
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Negation(expression.compile(gramat, compiled));
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return expression.isPlain(gramat);
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return !expression.isOptional(gramat);
    }

    // parsing

    public static GNegation expectNegation(Tape tape) {
        expectSymbol(tape, '!');

        skipVoid(tape);

        GElement expression = expectExpressionUnit(tape);

        return new GNegation(expression);
    }
}
