package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.Tape;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Repetition;
import org.bakasoft.gramat.Gramat;

import java.util.*;

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
    public List<GElement> getChildren() {
        if (separator == null) {
            return Arrays.asList(expression);
        }

        return Arrays.asList(expression, separator);
    }

    @Override
    public GElement simplify() {
        GElement simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        GElement simpleSeparator = separator != null ? separator.simplify() : null;
        int min = minimum != null ? minimum : 0;
        int max = maximum != null ? maximum : 0;

        // no repetition at all
        if (min == 1 && max == 1) {
            return simpleExpression;
        }
        // optional substitution
        else if (min == 0 && max == 1) {
            return new GOptional(simpleExpression).simplify();
        }

        // TODO add no separator simplification

        return new GRepetition(minimum, maximum, simpleExpression, simpleSeparator);
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
}
