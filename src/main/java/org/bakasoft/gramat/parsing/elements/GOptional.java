package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Optional;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.*;

public class GOptional extends GExpression1C {

    public GOptional(LocationRange location, GExpression expression) {
        super(location, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        // double optional simplification
        if (simpleExpression instanceof GOptional) {
            return simpleExpression;
        }

        return new GOptional(location, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Optional(expression.compile(compiled));
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return true;
    }

    @Override
    public void validate_r(GControl control) {
        if (expression.hasWildProducers()) {
            // TODO validate this
            throw new GrammarException("There cannot be producers inside optionals, consider wrapping them with mutations.", expression.location);
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
