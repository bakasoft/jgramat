package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Termination;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaEntity;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GTerminator extends GExpression0C {

    public GTerminator(LocationRange location, Gramat gramat) {
        super(location, gramat);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaEntity parentEntity, SchemaField parentField) {
        return new SchemaType(); // empty type
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
