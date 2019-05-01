package org.gramat.parsing.elements;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Negation;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression1C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GNegation extends GExpression1C {

    public GNegation(LocationRange location, GExpression expression) {
        super(location, expression);
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        if (expression.isOptional()) {
            throw new GrammarException("Optional expressions are not allowed inside negations to avoid infinite loops.", expression.location);
        }

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
        // TODO verify that negations should not contain producers
        return expression.generateSchemaType(control, parentType, parentField);
    }
}
