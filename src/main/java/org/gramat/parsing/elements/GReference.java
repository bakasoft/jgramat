package org.gramat.parsing.elements;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Reference;
import org.gramat.Gramat;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression0C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GReference extends GExpression0C {

    public final String ruleName;

    public GReference(LocationRange location, Gramat gramat, String ruleName) {
        super(location, gramat);
        this.ruleName = Objects.requireNonNull(ruleName);
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Reference(ruleName);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        return control.add(expression) && expression.isOptional_r(control);
    }

    @Override
    public void validate_r(GControl control) {
        // TODO validate infinite loops
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        if (control.add(expression)) {
            expression.countWildProducers_r(count, control);
        }
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        if (control.add(expression)) {
            expression.countWildMutations_r(count, control);
        }
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return control.reference(this, parentType, parentField);
    }
}
