package org.bakasoft.gramat.parsing.elements;

import org.bakasoft.gramat.LocationRange;
import org.bakasoft.gramat.elements.Element;
import org.bakasoft.gramat.elements.Reference;
import org.bakasoft.gramat.Gramat;
import org.bakasoft.gramat.parsing.GExpression;
import org.bakasoft.gramat.parsing.util.GControl;
import org.bakasoft.gramat.parsing.util.GExpression0C;

import java.util.*;

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
    public boolean hasWildProducers_r(GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        return control.add(expression) && expression.hasWildProducers_r(control);
    }

    @Override
    public boolean hasWildMutations_r(GControl control) {
        GExpression expression = gramat.findExpression(ruleName);
        return control.add(expression) && expression.hasWildMutations_r(control);
    }

}
