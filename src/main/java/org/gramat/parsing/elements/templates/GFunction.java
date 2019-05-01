package org.gramat.parsing.elements.templates;

import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression1C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GFunction extends GExpression1C {

    public final String name;
    public final String[] arguments;

    public GFunction(LocationRange location, String name, String[] arguments, GExpression expression) {
        super(location, expression);
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GFunction(location, name, arguments, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }

    @Override
    public void validate_r(GControl control) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }
}
