package org.gramat.parsing.elements.mutations;

import org.gramat.Gramat;
import org.gramat.GrammarException;
import org.gramat.LocationRange;
import org.gramat.elements.DynamicProperty;
import org.gramat.elements.Element;
import org.gramat.parsing.GExpression;
import org.gramat.parsing.util.GControl;
import org.gramat.parsing.util.SchemaControl;
import org.gramat.schema.SchemaField;
import org.gramat.schema.SchemaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GDynamicProperty extends GMutation {

    public final GExpression nameExpression;
    public final GExpression separatorExpression;
    public final GExpression valueExpression;
    public final boolean appendMode;

    // Normal mode: key-value, inverted mode: value-key
    public final boolean invertedMode;

    public GDynamicProperty(LocationRange location, Gramat gramat, GExpression nameExpression, GExpression separatorExpression, GExpression valueExpression, boolean appendMode, boolean invertedMode) {
        super(location, gramat);
        this.nameExpression = nameExpression;
        this.separatorExpression = separatorExpression;
        this.appendMode = appendMode;
        this.valueExpression = valueExpression;
        this.invertedMode = invertedMode;
    }

    @Override
    public List<GExpression> getChildren() {
        if (separatorExpression == null) {
            return Arrays.asList(nameExpression, valueExpression);
        }

        return Arrays.asList(nameExpression, separatorExpression, valueExpression);
    }

    @Override
    public GExpression simplify() {
        return new GDynamicProperty(
                location, gramat,
                nameExpression.simplify(),
                separatorExpression != null ? separatorExpression.simplify() : null,
                valueExpression.simplify(),
                appendMode,
                invertedMode
        );
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        Element cName = nameExpression.compile(compiled);
        Element cSeparator = separatorExpression != null ? separatorExpression.compile(compiled) : null;
        Element cValue = compileValue(valueExpression, compiled);

        return new DynamicProperty(cName, cSeparator, cValue,  appendMode, invertedMode);
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return getChildren().stream().allMatch(e -> e.isOptional_r(control));
    }

    @Override
    public void validate_r(GControl control) {
        if (nameExpression.countWildMutations() + nameExpression.countWildProducers() > 0) {
            throw new GrammarException("Property names cannot have mutations or producers inside.", nameExpression.location);
        }
        else if (separatorExpression != null && (separatorExpression.countWildMutations() + separatorExpression.countWildProducers() > 0)) {
            throw new GrammarException("Property separators cannot have mutations or producers inside.", separatorExpression.location);
        }

        validateValueExpression(valueExpression);
    }

    @Override
    public SchemaType generateSchemaType(SchemaControl control, SchemaType parentType, SchemaField parentField) {
        return generateSchemaType(null, location, valueExpression, appendMode,
            parentType, parentField, control);
    }

}
