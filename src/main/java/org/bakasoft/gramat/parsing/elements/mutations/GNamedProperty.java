package org.bakasoft.gramat.parsing.elements.mutations;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.NamedProperty;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.SchemaControl;
import org.bakasoft.gramat.schema.SchemaField;
import org.bakasoft.gramat.schema.SchemaType;

import java.util.*;

public class GNamedProperty extends GMutation {

    public final String propertyName;
    public final boolean appendMode;
    public final GExpression expression;

    public GNamedProperty(LocationRange location, String propertyName, boolean appendMode, GExpression expression) {
        super(location, expression.gramat);
        this.propertyName = propertyName;
        this.appendMode = appendMode;
        this.expression = expression;
    }

    @Override
    public GExpression simplify() {
        GExpression simpleExpression = expression.simplify();

        if (simpleExpression == null) {
            return null;
        }

        return new GNamedProperty(location, propertyName, appendMode, simpleExpression);
    }

    @Override
    public List<GExpression> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Element cValue = compileValue(expression, compiled);

        return new NamedProperty(
                propertyName,
                appendMode,
                cValue);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return expression.isOptional_r(control);
    }

    @Override
    public void validate_r(GControl control) {
        validateValueExpression(expression);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return generateSchemaType(propertyName, location, expression, appendMode,
            parentType, parentField, control);
    }
}
