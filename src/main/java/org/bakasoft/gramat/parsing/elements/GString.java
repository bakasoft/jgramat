package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.SingleChar;
import org.bakasoft.gramat.elements.Symbol;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

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
