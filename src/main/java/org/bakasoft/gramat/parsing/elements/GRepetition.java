package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Repetition;
import org.bakasoft.gramat.parsing.GElement;
import org.bakasoft.gramat.Gramat;

import java.util.Map;
import java.util.Objects;

public class GRepetition extends GElement {

    public final Integer minimum;
    public final Integer maximum;
    public final GElement expression;
    public final GElement separator;

    public GRepetition(Integer minimum, Integer maximum, GElement expression, GElement separator) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.expression = Objects.requireNonNull(expression);
        this.separator = separator;
    }

    @Override
    public GElement simplify() {
        return this;
    }

    @Override
    public Element compile(Gramat gramat, Map<String, Element> compiled) {
        return new Repetition(
                expression.compile(gramat, compiled),
                minimum != null ? minimum : 0,
                maximum != null ? maximum : 0,
                separator != null ? separator.compile(gramat, compiled) : null
        );
    }

    @Override
    public boolean isPlain(Gramat gramat) {
        return expression.isPlain(gramat)
                && (separator == null || separator.isPlain(gramat));
    }

    @Override
    public boolean isOptional(Gramat gramat) {
        return minimum == null || minimum == 0 || expression.isOptional(gramat);
    }

    public static GRepetition expectRepetition(Tape tape) {
        expectSymbol(tape, '{');

        skipVoid(tape);

        Integer minimum = tryInteger(tape);
        Integer maximum;

        if (minimum != null) {
            skipVoid(tape);

            if (trySymbol(tape, ',')) {
                skipVoid(tape);

                maximum = expectInteger(tape);

                skipVoid(tape);
            }
            else {
                maximum = null;
            }
        }
        else {
            maximum = null;
        }

        GElement expression = expectExpression(tape);
        GElement separator;

        skipVoid(tape);

        if (trySymbol(tape, '/')) {
            skipVoid(tape);

            separator = expectExpression(tape);

            skipVoid(tape);
        }
        else {
            separator = null;
        }

        expectSymbol(tape, '}');

        return new GRepetition(minimum, maximum, expression, separator);
    }
}
