package org.bakasoft.gramat.parsing.elements.templates;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaEntity;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

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
    public SchemaType generateSchemaType(SchemaControl control, SchemaEntity parentEntity, SchemaField parentField) {
        throw new GrammarException("Functions are not implemented yet.", location);
    }
}
