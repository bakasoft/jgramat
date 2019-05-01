package org.gramat.parsing.elements;

import org.gramat.LocationRange;
import org.gramat.elements.Element;
import org.gramat.elements.SingleChar;
import org.gramat.elements.Symbol;
import org.gramat.Gramat;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.GExpression0C;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GString extends GExpression0C {

    public final String content;

    public GString(LocationRange location, Gramat gramat, String content) {
        super(location, gramat);
        this.content = Objects.requireNonNull(content);
    }

    @Override
    public GExpression simplify() {
        return this;
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        if (content.isEmpty()) {
            return null;
        }
        else if (content.length() == 1) {
            char c = content.charAt(0);

            return new SingleChar(c);
        }

        return new Symbol(content);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return content.isEmpty();
    }

    @Override
    public void validate_r(GControl control) {
        // nothing to validate yet
    }

    @Override
    public void countWildProducers_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

    @Override
    public void countWildMutations_r(AtomicInteger count, GControl control) {
        // nothing to count
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return null; // empty type
    }
}
