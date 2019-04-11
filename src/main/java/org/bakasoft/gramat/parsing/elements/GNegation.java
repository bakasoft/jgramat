package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Negation;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.Map;

public class GNegation extends GExpression1C {

    public GNegation(LocationRange location, GExpression expression) {
        super(location, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        // remove double negation
        if (simpleExpression instanceof GNegation) {
            return ((GNegation)simpleExpression).expression;
        }

        return new GNegation(location, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Negation(expression.compile(compiled));
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return false; // TODO verify that negations can't be skipped
    }

    @Override
    public void validate_r(GControl control) {
        // TODO verify that negations should not contain producers
        if (expression.isOptional()) {
            throw new GrammarException("Optional expressions are not allowed inside negations to avoid infinite loops.", expression.location);
        }
    }

    @Override
    public boolean hasWildProducers_r(GControl control) {
        return expression.hasWildProducers_r(control);
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        return expression.hasWildMutations_r(control);
    }

}
