package org.gramat.parsing.elements;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.Termination;
import org.gramat.Gramat;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression0C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GTerminator extends GExpression0C {

    public GTerminator(LocationRange location, Gramat gramat) {
        super(location, gramat);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return null; // empty type
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new Termination();
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return false;
    }

    @Override
    public void validate_r(GControl control) {
        // everything is ok!
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

}
