package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Repetition;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GRepetition extends GExpression {

    public final int minimum;
    public final int maximum;
    public final GExpression expression;
    public final GExpression separator;

    public GRepetition(LocationRange location, Gramat gramat, Integer minimum, Integer maximum, GExpression expression, GExpression separator) {
        super(location, gramat);
        this.minimum = minimum != null ? minimum : 0;
        this.maximum = maximum != null ? maximum : 0;
        this.expression = Objects.requireNonNull(expression);
        this.separator = separator;
    }

    @Override
    public List<GExpression> getChildren() {
        if (separator == null) {
            return Collections.singletonList(expression);
        }

        return Arrays.asList(expression, separator);
    }

    @Override
    public GExpression simplify() {
        GExpression simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        GExpression simpleSeparator = separator != null ? separator.simplify() : null;

        // no repetition at all
        if (minimum == 1 && maximum == 1) {
            return simpleExpression;
        }
        // optional substitution
        else if (minimum == 0 && maximum == 1) {
            return new GOptional(location, simpleExpression).simplify();
        }

        // TODO add no separator simplification

        return new GRepetition(location, gramat, minimum, maximum, simpleExpression, simpleSeparator);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Repetition(
                expression.compile(compiled), minimum, maximum,
                separator != null ? separator.compile(compiled) : null
        );
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return minimum == 0 || expression.isOptional_r(control);
    }

    @Override
    public void validate_r(GControl control) {
        if (expression.isOptional()) {
            throw new GrammarException("Optional expressions are not allowed inside repetitions to avoid infinite loops.", expression.location);
        }
        else if (expression.countWildProducers() > 0) {
            throw new GrammarException("There cannot be producers inside repetitions, consider wrapping them with mutations.", expression.location);
        }
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        expression.countWildProducers_r(count, control);
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        expression.countWildMutations_r(count, control);
    }

}
