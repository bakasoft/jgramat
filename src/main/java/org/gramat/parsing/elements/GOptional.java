package org.gramat.parsing.elements;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Optional;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression1C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
//        if (expression.countWildProducers() > 0) {
//            // TODO validate this
//            throw new GrammarException("There cannot be producers inside optionals, consider wrapping them with mutations.", expression.location);
//        }
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        expression.countWildProducers_r(count, control);
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        expression.countWildMutations_r(count, control);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        if (expression.generateSchemaType(control, parentType, parentField) != null) {
            // TODO validate this
            throw new GrammarException("There cannot be producers inside optionals, consider wrapping them with mutations.", expression.location);
        }

        return null; // empty type
    }
}
