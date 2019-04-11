package org.bakasoft.gramat.parsing.elements.mutations;

import org.bakasoft.gramat.GrammarException;
import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.NamedProperty;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression1C;

import java.util.*;

public class GNamedProperty extends GExpression1C {

    public final String propertyName;
    public final boolean appendMode;

    public GNamedProperty(LocationRange location, String propertyName, boolean appendMode, GExpression expression) {
        super(location, expression);
        this.propertyName = propertyName;
        this.appendMode = appendMode;
    }

    @Override
    public GExpression simplify(GExpression simpleExpression) {
        return new GNamedProperty(location, propertyName, appendMode, simpleExpression);
    }

    @Override
    public Element compile(Map<String, Element> compiled) {
        return new NamedProperty(
                propertyName,
                appendMode,
                expression.compile(compiled));
    }

    @Override
    public boolean isOptional_r(GControl control) {
        return expression.isOptional_r(control);
    }

    @Override
    public void validate_r(GControl control) {
        if (expression.hasWildMutations()) {
            throw new GrammarException("Property values cannot have other mutations inside.", expression.location);
        }
    }

    @Override
    public boolean hasWildProducers_r(GControl control) {
        // properties absorb producers
        return false;
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        // this is a wild mutation!
        return true;
    }

}
